package org.example;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.values.PCollection;

public class Example {
    public static void main(String[] args) {
        PipelineOptions pipelineOptions= PipelineOptionsFactory.create();
        Pipeline pipeline=Pipeline.create(pipelineOptions);
        PCollection<String> allString=pipeline.apply(Create.of("hello","world","Hi","India"));

    }
}
