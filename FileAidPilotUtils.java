////////////////////////////////////////////////////////////////////////////////
//  Course:   CSC 289 Spring 2018
//  Section:  0001
// 
//  Project:  FileAidPilot
//  File:     FileAidPilotUtils.java
//  
//  Name:     David Matthews
//  Email:    dlmatthews1@my.waketech.edu
////////////////////////////////////////////////////////////////////////////////

/*
     Adapted by David Matthews to assist with the FileAid Pilot program
     
     Derby - WwdUtils.java - utility methods used by WwdEmbedded.java

        Licensed to the Apache Software Foundation (ASF) under one
           or more contributor license agreements.  See the NOTICE file
           distributed with this work for additional information
           regarding copyright ownership.  The ASF licenses this file
           to you under the Apache License, Version 2.0 (the
           "License"); you may not use this file except in compliance
           with the License.  You may obtain a copy of the License at

             http://www.apache.org/licenses/LICENSE-2.0

           Unless required by applicable law or agreed to in writing,
           software distributed under the License is distributed on an
           "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
           KIND, either express or implied.  See the License for the
           specific language governing permissions and limitations
           under the License.    

*/

import java.io.*;
import java.sql.*;
public class FileAidPilotUtils {

/***      Check for  FILE table    ****/
   public static boolean wwdChk4Table (Connection conTst ) throws SQLException {
      boolean chk = true;
      boolean doCreate = false;
      try {
         Statement s = conTst.createStatement();
         s.execute("update FILE set fiID = 999, fiPATH = 'C:\', fiNAME = 'TEST ENTRY', fiSIZE = 1, fiEXTENSION = '.DOCX',"
         		+ " fiACTIVE = 1, fiMEMO = 'Test' where 1=3");
      }  catch (SQLException sqle) {
         String theError = (sqle).getSQLState();
         /** If table exists will get -  WARNING 02000: No row was found **/
         if (theError.equals("42X05"))   // Table does not exist
         {  return false;
          }  else if (theError.equals("42X14") || theError.equals("42821"))  {
             System.out.println("WwdChk4Table: Incorrect table definition. Drop table FILE and rerun this program");
             throw sqle;   
          } else { 
             System.out.println("WwdChk4Table: Unhandled SQLException" );
             throw sqle; 
          }
      }
      //  System.out.println("Just got the warning - table exists OK ");
      return true;
   }  /*** END wwdInitTable  **/
}

