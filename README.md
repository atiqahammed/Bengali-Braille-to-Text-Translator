## Project Specification
Braille is a reading and writing system which can only be read with the sense of touch. It is used by blind and visually impaired people who cannot access print materials. Braille is not a language. Rather, it is a code by which many languages can be written and read.  It uses raised dots to represent the letters of the print alphabet. It also includes symbols to represent punctuation.
This project aims to extract Bengali text from a scanned image of the paper on which Bengali braille code is typed.

## Project Overview
The main focused of this project is to develop a system for converting Bangla Braille document into its equivalent natural language characters and words which is called Optical Braille Recognition (OBR). It involves two main steps like Recognition of braille cell and transcription of the Braille cell. The first step involved a few pre-processing steps, dot and cell recognition, etc. Second step aimed at converting the segmented Braille character into its natural language character. The following methods will be applied to develop the system.
-	Preprocessing
	1.	Converting the image to Gray Level 
	2.	Image thresholding 
	3.	Converting image to binary image 
- 	Braille Characters Identification
	1.	Dot identification
	2.	Line identification
	3.	Braille cell identification
	4.	Character identification
-	Post Processing
	1.	Word identification
	2.	Spell checking
	3.	Bangla text generation
	
## System Requirements
- This is application will work on windows 7, 8 and 10. 
- As the system was developed in Java 10. JDK 10 or upper version should be installed in the system.
- Check the system variable has “JAVA_HOME” with the path of the Java JDK directory. It it not exists then add it to the system variable.
- Check the environment variable has the Jdk and Jre director path. If it not exists then add those in the environment variable path.
- Install openvc version opencv-4.1.1 and copy the opencv_java411.dll file and add it "C:\Program Files (x86)\Common Files\Oracle\Java\javapath" directory. 
