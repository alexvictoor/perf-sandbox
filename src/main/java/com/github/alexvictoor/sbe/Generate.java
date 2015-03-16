package com.github.alexvictoor.sbe;

import uk.co.real_logic.sbe.SbeTool;

import java.io.File;

public class Generate {

    public static void main(String[] args) throws Exception {
        System.out.println(new File("exemple-schema.xml").getAbsolutePath());
        SbeTool.main(new String[] {"src/main/resources/exemple-schema.xml"});
    }

}
