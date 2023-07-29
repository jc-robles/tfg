package com.imudataprocessor.model.service;

import com.imudataprocessor.api.service.ExternalProcess;
import com.imudataprocessor.api.service.InternalDataDTO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Service
public class ExternalProcessImpl implements ExternalProcess /*, CommandLineRunner*/ {

    @Override
    public void execute(final InternalDataDTO internalDataDTO) throws IOException {
//        String[] a = new String[]{"0.2015380859375","0.1963958740234375","0.1940155029296875"};
//        String[] b = new String[]{"0.9995574951171875","0.9622650146484375","0.9890594482421875"};
//        String[] c = new String[]{"-0..0761566162109375","-0.088714599609375","-0.047332763671875"};




        ProcessBuilder processBuilder = new ProcessBuilder("python", "src/main/resources/python/2_min_walk.py"
//                ,Arrays.toString(internalDataDTO.getAccelerometerX().toArray())
//                , Arrays.toString(internalDataDTO.getAccelerometerY().toArray())
//                , Arrays.toString(internalDataDTO.getAccelerometerZ().toArray())
        );
        processBuilder.redirectErrorStream(true);


        File fileInput = new File("input.json");
        File file = new File("output.json");
        processBuilder.redirectOutput(file);
        processBuilder.redirectInput(fileInput);

        Process process = processBuilder.start();
    }

//    @Override
//    public void run(String... args) throws Exception {
//        this.execute("");
//    }
}
