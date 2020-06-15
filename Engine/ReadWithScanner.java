/*
 Copyright (c) 2002-2018 Hirondelle Systems.
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

     * Redistributions of source code must retain the above copyright
       notice, this list of conditions and the following disclaimer.
     * Redistributions in binary form must reproduce the above copyright
       notice, this list of conditions and the following disclaimer in the
       documentation and/or other materials provided with the distribution.
     * Neither the name of Hirondelle Systems nor the
       names of its contributors may be used to endorse or promote products
       derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY HIRONDELLE SYSTEMS ''AS IS'' AND ANY
 EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL HIRONDELLE SYSTEMS BE LIABLE FOR ANY
 DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
//This example demonstrates using Scanner to read a file containing lines of structured data. One Scanner is used to read in each line, and a second Scanner is used to parse each line into a simple name-value pair. The Scanner class is only used for reading, not for writing.
package Engine;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;

/** Assumes UTF-8 encoding. JDK 7+. */
public final class ReadWithScanner {

  public static void main(String... args) throws IOException {
    ReadWithScanner parser = new ReadWithScanner("C:\\Temp\\test.txt");
    parser.processLineByLine();
    log("Done.");
  }
  
  /**
   Constructor.
   @param fileName full name of an existing, readable file.
  */
  public ReadWithScanner(String fileName){
    filePath = Paths.get(fileName);
  }
  
  
  /** Template method that calls {@link #processLine(String)}.  */
  public final void processLineByLine() throws IOException {
    try (Scanner scanner =  new Scanner(filePath, ENCODING.name())){
      while (scanner.hasNextLine()){
        processLine(scanner.nextLine());
      }      
    }
  }
  
  /** 
   Overridable method for processing lines in different ways.
    
   <P>This simple default implementation expects simple name-value pairs, separated by an 
   '=' sign. Examples of valid input: 
   <tt>height = 167cm</tt>
   <tt>mass =  65kg</tt>
   <tt>disposition =  "grumpy"</tt>
   <tt>this is the name = this is the value</tt>
  */
  protected void processLine(String line){
    //use a second Scanner to parse the content of each line 
    try(Scanner scanner = new Scanner(line)){
      scanner.useDelimiter("=");
      if (scanner.hasNext()){
        //assumes the line has a certain structure
        String name = scanner.next();
        String value = scanner.next();
        log("Name is : " + quote(name.trim()) + ", and Value is : " + quote(value.trim()));
      }
      else {
        log("Empty or invalid line. Unable to process.");
      }
    }
  }
  
  // PRIVATE 
  private final Path filePath;
  private final static Charset ENCODING = StandardCharsets.UTF_8;  
  
  private static void log(Object object){
    System.out.println(Objects.toString(object));
  }
  
  private String quote(String text){
    String QUOTE = "'";
    return QUOTE + text + QUOTE;
  }
} 
