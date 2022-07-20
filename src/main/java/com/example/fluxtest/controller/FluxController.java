package com.example.fluxtest.controller;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.fluxtest.interpreter.PyFunction;
import com.example.fluxtest.model.FuncResult;
import com.example.fluxtest.payload.OrderedResult;
import com.example.fluxtest.payload.UnorderedResult;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/api")
@PropertySource("classpath:application.properties")
public class FluxController {

    private Long period = 1000L;

    public FluxController(Environment environment) {
        String periodStr = environment.getProperty("calculation.period.ms");
        if (periodStr != null) {
            try {
                period = Long.valueOf(periodStr);
            } catch (NumberFormatException e) {
                System.err.println("calculation.period must be a number");
            }
        }
    }

    @CrossOrigin
    @GetMapping(path = "/call-func", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> calc(@RequestParam("func1") String func1, @RequestParam("func2") String func2,
                                        @RequestParam("count") Integer count, @RequestParam("ordered") Boolean ordered) {
        AtomicInteger cnt1 = new AtomicInteger();
        AtomicInteger cnt2 = new AtomicInteger();
        PyFunction function1;
        PyFunction function2;
        try {
            function1 = new PyFunction(func1);
            function2 = new PyFunction(func2);
        } catch (RuntimeException e) {
            return Flux.just(e.getMessage());
        }
        Flux<FuncResult> flux1 = Flux.range(0, count)
                .delayElements(Duration.ofMillis(period))
                .map((seq) -> function1.execute(seq).setFuncNum(1))
                .doOnEach((e) -> cnt1.getAndIncrement())
                .take(count)
                .publishOn(Schedulers.newSingle("first-x"));
        Flux<FuncResult> flux2 = Flux.range(0, count)
                .delayElements(Duration.ofMillis(period))
                .map((seq) -> function2.execute(seq).setFuncNum(2))
                .doOnEach((e) -> cnt2.getAndIncrement())
                .take(count)
                .publishOn(Schedulers.newSingle("second-x"));
        if (ordered) {
            return Flux.zip(flux1, flux2, (elem1, elem2) -> new OrderedResult(elem1.getIterNum(),
                    elem1.getResult(), elem1.getTime().intValue(), cnt1.get() - elem1.getIterNum() - 1,
                    elem2.getResult(), elem2.getTime().intValue(), cnt2.get() - elem2.getIterNum() - 1).toCSV());
        } else {
            return Flux.merge(flux1, flux2)
                    .map(elem -> new UnorderedResult(elem.getIterNum(),
                            elem.getFuncNum(), elem.getResult(), elem.getTime().intValue()).toCSV());
        }
    }
}
