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


## Scenario
Bengali Braille to Text Translator will be a desktop application for Windows operating system. It will be a tool that will take a scanned image of Bengali Braille writing as input. The system will be able to support all popular image formats like- JPG, JPEG, PNG. The input will go through different types of image-preprocessing techniques, translate Braille to text through pattern recognition, and apply text correction procedures for final output. The methodology of whole process will be discussed later. Then it will provide the Bengali text that is written in the scanned input image. If the user wants to save the text file in the local directory of the computer our system will provide the user to save it.

### Choosing Image File
After launching the application, the user will have a user interface from which she/he will get a file choosing option. Using that option, the user can will be able to choose an image using Windows file chooser. The chosen image will be the input for the application.

### Selecting Language
The user will get an option to choose a language between Bengali and English for translating the input image into text. By default, the application will provide Bengali language as chosen one. If the user wants to translate the image into English language, she/he have to choose the English option.

### Image to Text Translation
The user will then click on the translate button. After some processing on the image the application will show the corresponding output in the user interface of the application.

### Saving Output
The user will get an option to save the output in a file with “.txt” extension. After clicking on the save button on the user interface the user will get an file saving option to save the file in her/his chosen directory.

	
## System Requirements
- This is application will work on windows 7, 8 and 10. 
- As the system was developed in Java 10. JDK 10 or upper version should be installed in the system.
- Check the system variable has “JAVA_HOME” with the path of the Java JDK directory. It it not exists then add it to the system variable.

![System Variable View](https://github.com/atiqahammed/Bengali-Braille-to-Text-Translator/blob/Documentation/documents/systemVeriable.PNG)

- Check the environment variable has the Jdk and Jre director path. If it not exists then add those in the environment variable path.

![Environment Variable View](https://github.com/atiqahammed/Bengali-Braille-to-Text-Translator/blob/Documentation/documents/env.PNG)

- Install openvc version opencv-4.1.1 and copy the opencv_java411.dll file and add it "C:\Program Files (x86)\Common Files\Oracle\Java\javapath" directory. 
