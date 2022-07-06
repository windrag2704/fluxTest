package com.example.fluxtest.controller;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fluxtest.model.FuncResult;
import com.example.fluxtest.payload.CalculationResult;
import com.example.fluxtest.payload.OrderedResult;
import com.example.fluxtest.payload.UnorderedResult;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import static java.time.Instant.now;

@RestController
@RequestMapping("/api")
public class FluxController {

    @GetMapping(value = "/test", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> test() {
        return Flux.generate(() -> 0, (state, sink) -> {
            sink.next("Value: " + state);
            if (state > 10) {
                sink.complete();
            }
            return state + 1;
        });
    }

    final Sinks.Many<String> sink;
    final AtomicLong counter;

    public FluxController() {
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
        this.counter = new AtomicLong();
    }

    @GetMapping("/send")
    public void send() {
        Sinks.EmitResult result = sink.tryEmitNext("Hello World #" + counter.getAndIncrement());

        if (result.isFailure()) {
            // do something here, since emission failed
        }
    }

    @RequestMapping(value = "sub", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> sse() {
        return sink.asFlux().map(e -> ServerSentEvent.builder(e).build());
    }

    @GetMapping("/stop")
    public void stop() {
        sink.tryEmitComplete();
    }

    @GetMapping(path = "/stream-flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamFlux() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> "Flux - " + LocalTime.now().toString());
    }

    @GetMapping(path = "/call-func", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<CalculationResult> calc() {
        Random random = new Random();
        boolean ordered = false;
        Function<Long, Long> func1 = (num) -> num * num;
        Function<Long, Long> func2 = (num) -> num + num;
        BiFunction<Long, Function<Long, Long>, FuncResult> calculate = (seq, func) -> {
            long start = Instant.now().toEpochMilli();
            try {
                Thread.sleep(random.nextInt(10000) + 500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long result = func.apply(seq);
            long end = Instant.now().toEpochMilli();
            return new FuncResult(seq.intValue(), result, end - start);
        };
        int count = 10;
        AtomicInteger cnt1 = new AtomicInteger();
        AtomicInteger cnt2 = new AtomicInteger();
        Flux<FuncResult> flux1 = Flux.range(0, count)
                .delayElements(Duration.ofSeconds(1))
                .map((seq) -> calculate.apply(seq.longValue(), func1).setFuncNum(1))
                .doOnEach((e) -> cnt2.getAndIncrement())
                .take(count)
                .publishOn(Schedulers.newSingle("first-x"));
        Flux<FuncResult> flux2 = Flux.range(0, count)
                .delayElements(Duration.ofSeconds(1))
                .map((seq) -> calculate.apply(seq.longValue(), func2).setFuncNum(2))
                .doOnEach((e) -> cnt1.getAndIncrement())
                .take(count)
                .publishOn(Schedulers.newSingle("second-x"));
        if (ordered) {
            return Flux.zip(flux1, flux2, (elem1, elem2) -> new OrderedResult(elem1.getIterNum(),
                    elem1.getResult().intValue(), elem1.getTime().intValue(), cnt1.get() - elem1.getIterNum() - 1,
                    elem2.getResult().intValue(), elem2.getTime().intValue(), cnt2.get() - elem2.getIterNum() - 1));
        } else {
            return Flux.merge(flux1, flux2)
                    .map(elem -> new UnorderedResult(elem.getIterNum(),
                            elem.getFuncNum(), elem.getResult().intValue(), elem.getTime().intValue()));
        }
    }
}
