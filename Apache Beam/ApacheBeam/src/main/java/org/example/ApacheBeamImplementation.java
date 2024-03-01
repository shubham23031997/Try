package org.example;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.*;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;

import java.util.Arrays;
import java.util.List;


public class ApacheBeamImplementation {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Options options = PipelineOptionsFactory.fromArgs(args).withValidation().as(Options.class);
        Pipeline p = Pipeline.create(options);

        //starts by defining the options for the pipelines
        //to configure different aspects of pipeline such as pipeline runner
//(1)--------------------------------------------------------------------------------
        PipelineOptions pipelineOptions = PipelineOptionsFactory.create();

        //then create the pipeline
        Pipeline pipeline = Pipeline.create(pipelineOptions);
//        final List<String> input= Arrays.asList("one","second","third","forth","fifth");
//        pipeline.apply(Create.of(input)).apply(TextIO.write().to("G:\\Practice tasks\\Apache Beam\\ApacheBeam\\output\\example").withSuffix(".txt"));
        //Create.of()...it takes collection as input and convert it into PCollection

//(2)--------------------------------------------------------------------------------
        PCollection<String> allStrings = pipeline.apply(Create.of("Hello", "world", "hi"));
        PCollection<String> longStrings = allStrings
                .apply(Filter.by(new SerializableFunction<String, Boolean>() {
                    @Override
                    public Boolean apply(String input) {
                        return input.length() > 3;
                    }
                }));

        longStrings.apply(ParDo.of(new DoFn<String, Void>() {
            @ProcessElement
            public void longString(ProcessContext c) {
                System.out.println(c.element());
            }
        }));

//(3)--------------------------------------------------------------------------------
//        using less than ,greater than, divisible by
        PCollection<Integer> number = pipeline.apply(Create.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        PCollection<Integer> output = applyTransformation(number);
        PCollection<Integer> greaterThan = number.apply(Filter.greaterThan(7));
        PCollection<Integer> lessThan = number.apply(Filter.lessThanEq(7));
//        System.out.println(greaterThan);
        greaterThan.apply(ParDo.of(new DoFn<Integer, Void>() {
            @ProcessElement
            public void greaterThan(ProcessContext c) {
                System.out.println("greater than 7 : " + c.element());
            }
        }));
        lessThan.apply(ParDo.of(new DoFn<Integer, Void>() {
            @ProcessElement
            public void lessThan(ProcessContext c) {
                System.out.println("less than or equal to 7 : " + c.element());
            }
        }));

        output.apply(ParDo.of(new DoFn<Integer, Void>() {
            @ProcessElement
            public void divisibleByTwo(ProcessContext processContext) {
                System.out.println("numbers divisible by 2 : " + processContext.element());
            }
        }));
//(4)--------------------------------------------------------------------------------
        //  Use FlatMapElements to split the lines in PCollection<String> into individual words.
        PCollection<String> lines = pipeline.apply(Create.of("the quick brown fox", "jumps over the lazy", "dog"));
        PCollection<String> words =
                lines.apply(FlatMapElements.via(new InferableFunction<String, List<String>>() {
                    @Override
                    public List<String> apply(String line) {
                        return Arrays.asList(line.split(" "));
                    }
                }));
        words.apply(ParDo.of(new DoFn<String, Void>() {
            @ProcessElement
            public void flatteningSentenceIntoWord(ProcessContext c) {
                System.out.println("flattening the sentence into words : " + c.element());
            }
        }));
//(5)--------------------------------------------------------------------------------

        PCollection<KV<String, Integer>> pairs = pipeline.apply(Create.of(KV.of("one", 1), KV.of("two", 2), KV.of("three", 3), KV.of("four", 4)));
        PCollection<String> keys = pairs.apply(Keys.create());
        keys.apply(ParDo.of(new DoFn<String, Void>() {
            @ProcessElement
            public void printingKeys(ProcessContext c) {
                System.out.println("only keys of above pairs : " + c.element());
            }
        }));

//(6)--------------------------------------------------------------------------------
        PCollection<Integer> values = pairs.apply(Values.create());
        values.apply(ParDo.of(new DoFn<Integer, Void>() {
            @ProcessElement
            public void printingValues(ProcessContext c) {
                System.out.println("only values of above pairs : " + c.element());
            }
        }));
//(7)--------------------------------------------------------------------------------
        //swapping keys and values of above
        PCollection<KV<Integer, String>> swapping = pairs.apply(KvSwap.create());
        swapping.apply(ParDo.of(new DoFn<KV<Integer, String>, Void>() {
            @ProcessElement
            public void printingValues(ProcessContext c) {
                System.out.println("swapping keys and values : " + c.element());
            }
        }));
//(8)--------------------------------------------------------------------------------

        PCollection<String> data1 = pipeline.apply(Create.of("shubham", "Aashay", "Kunal", "Sameer"));
        PCollection<String> upperCase = data1.apply(MapElements.via(new SimpleFunction<String, String>() {
            @Override
            public String apply(String input) {
                return input.toUpperCase();
            }
        }));
        upperCase.apply(ParDo.of(new DoFn<String, Void>() {
            @ProcessElement
            public void PrintingInUpperCase(ProcessContext c) {
                System.out.println("printing in upperCase : " + c.element());
            }
        }));

// (9)--------------------------------------------------------------------------------

        PCollection<String> lowerCase = data1.apply(MapElements.via(new SimpleFunction<String, String>() {
            @Override
            public String apply(String input) {
                return input.toLowerCase();
            }
        }));
        lowerCase.apply(ParDo.of(new DoFn<String, Void>() {
            @ProcessElement
            public void PrintingInLowerCase(ProcessContext c) {
                System.out.println("printing in lowercase : " + c.element());
            }
        }));

// (10)--------------------------------------------------------------------------------
//      using regex to mails
        PCollection<String> mails = pipeline.apply(Create.of("shubhammali", "mali@gmail.com", "khot123@gmail.com", "Ramesh@123yop@mail.com"));
        PCollection<String> properMails = mails.apply(Regex.matches("([a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6})"));
        properMails.apply(ParDo.of(new DoFn<String, Void>() {
            @ProcessElement
            public void properMails(ProcessContext c) {
                System.out.println("these are proper mails :" + c.element());
            }
        }));
// (10)--------------------------------------------------------------------------------

        PCollection<String> collection = pipeline
                .apply(TextIO.read().from("G:\\Practice tasks\\Apache Beam\\ApacheBeam\\input\\markSheet.csv"));

        PCollection<KV<String, Integer>> value = collection.apply(ParDo.of(new DoFn<String, KV<String, Integer>>() {
            @ProcessElement
            public void getTotalMarks(ProcessContext processContext) {
                if (!processContext.element().equals("Name,Marks")) {
                    String[] split = processContext.element().split(",");
                    String name = split[0];
                    int marks = Integer.parseInt(split[1]);
                    KV<String, Integer> sumOfMarks = KV.of(name, marks);
                    processContext.output(sumOfMarks);
                }
            }
        }));

        PCollection<KV<String, Integer>> sumofmarks = value.apply(Combine.perKey(Sum.ofIntegers()));
        sumofmarks.apply(ParDo.of(new DoFn<KV<String, Integer>, Void>() {
            @ProcessElement
            public void getSum(ProcessContext processContext) {
                System.out.println("sums of marks: " + processContext.element());
            }
        }));

// (11)--------------------------------------------------------------------------------

        //To Read the CSV file
        PCollection<String> data = pipeline.apply(TextIO.read().from("G:\\Practice tasks\\Apache Beam\\ApacheBeam\\input\\myFile.csv"));

        //transforming data as key as id and other fields as values
        PCollection<KV<String, String>> recordsDependOnId = data.apply(ParDo.of(new DoFn<String, KV<String, String>>() {
            @ProcessElement
            public void processFile(ProcessContext processContext) {
                String[] fields = processContext.element().split(",");
                String id = fields[0];
                String firstname = fields[1];
                String lastname = fields[2];
                String email = fields[3];
                processContext.output(KV.of(id, id + "," + firstname + "," + lastname + "," + email));
                //System.out.println(id + "," + firstname + "," + lastname + "," + email);
            }
        }));
//(5)--------------------------------------------------------------------------------
        // Example transformation: Apply uppercase transformation to firstname where ID is "839"
        PCollection<KV<String, String>> transformedRecords = recordsDependOnId
                .apply("TransformFirstName", ParDo.of(new DoFn<KV<String, String>, KV<String, String>>() {
                    @ProcessElement
                    public void processElement(ProcessContext processContext) {
                        KV<String, String> record = processContext.element();
                        String id = record.getKey();
                        String[] fields = record.getValue().split(",");
                        String firstname = fields[1];
                        String transformedFirstname = id.equals("839") ? firstname.toUpperCase() : firstname;
                        String lastname = fields[2];
                        String email = fields[3];
                        processContext.output(KV.of(id, id + "," + transformedFirstname + "," + lastname + "," + email));
//                        System.out.println(id+","+transformedFirstname+","+lastname+","+email);
                    }
                }));

        // Write the transformed records to output file
//        transformedRecords.apply(TextIO.write().to("G:\\Practice tasks\\Apache Beam\\ApacheBeam\\output\\transformed").withSuffix(".csv"));

        pipeline.run();
    }

    //method for applying filter to get numbers which divisible by 2
    static PCollection<Integer> applyTransformation(PCollection<Integer> input) {
        return input.apply(Filter.by(input1 -> input1 % 2 == 0));
    }
}

