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


## Methodology

### Step 1: Convert Image RGB to Gray (Grayscale Conversion): 
In any image, each of the pixels should have a variation of RGB values for three different colors Red, Green, and Blue. But for having the same value of each three colors for every pixel it requires to grayscale conversion for this image.

### Step 2: Noise Reduction:
For noise reduction median filter, gaussian elimination will be used. To apply gaussian blur 3 X 3 kernel was used. Same size kernel was used for median blur also.   

### Step 3: Binary conversion: 
Then it will take a thresholded image consisting of couples of white and black spots, where each couple denotes a single Braille dot. To do this Otsu thresholding will be used. In the binary image the white spots are considered as the dot in the image. And the black portion of the image are considered as the plain portion.

### Step 4: Collecting Dots
In this step, all separated white spots on the image are collected. To collect those white spots the connected white spots are considered as a single dot. For checking the neighbour dot to be part of the same dot it was checked as if the neighbour dot is white or not. If it was white and was not considered as a single dot then it was taken as part of it and mapped as checked one. To consider a pixel as a neighbour one 3 * 3 frame/kernel was used. 
 
### Step 5: Finding the Center of Collected Dots
To calculate the center of the rectangular was created using all the boundary pixel of a dot. The index of the cross point of two diagonals of the rectangle was considered as the center point of the dot.

### Step 6: Merging Dot
After considering those dots as a rectangle some rectangle could be overlapped. Those rectangles which were overlapped are then considered as a single dot after merging them. Then finally after merging those dots are remained those are considered as single dots.

### Step 7: Finding Line
Where a single dot is found in the image a line was considered there. All the dot nearest to the line was considered the those dots are also on the same line. The all the row index of all lines has been collected for post operation.

### Step 8: Merging Lines
All index of the collected lines then sorted to have a sequence of line. From the sorted collection sequential distance has been calculated. If there exist N line on the image from the list of N dot N-1 distance has been found. The the distance sorted. Form the the sorted distance we get a median of the distance. Those lines are then merged that  were within the half of the median distance.  

### Step 9: Mapping Dots with Lines
After getting the list of lines it requires to identify which dot exists on which line. Dots were then mapped with the nearest line index.

### Step 10: FInding Eligible Lines
To find the most eligible 3 dot lines for translating actual text format is the next step. To do so there require all the line to go through all the combination to be a part of the 3 eligible dot line. For example the first line only be the first line of a 3 sequential line. The second line can be the first line or it could be the second line of a 3 sequential eligible line for text translation. If we get multiple combination for a single line for being eligible then, it will be a matter of concern to find the most eligible line with which combination has the most dot count in it.

### Step 11: Column Identification
Find the column in the 3 sequential line that are selected in the previous steps. To Identify the column the half of the distance between line that was come from the median distance between all the sequential line	 is considered to be the threshold.

### Step 12: Word Identification
There are some threshold such as distance between column within same character and distance between column between different column. Identify separated words using those threshold. Two different words in a single line are in at least two distance between column in different character and one distance between within character.  

### Step 13: Braille Cell Identification  
Identify the braille cells in a word using the thresholds discussed in the previous step. If a column is in the first position it could be the first column of a cell or it could be the last column of a cell. To identify its main position in the cell all the combination should be considered to be a part of a cell. This combination will be done using the threshold. 

### Step 14: Decimal Braille Code Generation
This stage is a core stage of the system and it was done by testing each dot in Braille cell, if it active the position of this dot take digit one and if inactivated the position of this dot takes digit zero. The recognizing process of active or inactive dots was depended on taking summation of dot frame, if the summation was been one's digits, that means this dot is activated, else that dot will be inactive.

### Step 15: Character Recognition 
Using the corresponding binary code of every cell identify the corresponding character then Identified. To do so decimal braille code to character was used.   

### Step 16: Post Processing
In post process vowel and consonant are merged. Then concatenated character has been merged. 

	
## System Requirements
- This is application will work on windows 7, 8 and 10. 
- As the system was developed in Java 10. JDK 10 or upper version should be installed in the system.
- Check the system variable has “JAVA_HOME” with the path of the Java JDK directory. It it not exists then add it to the system variable.

![System Variable View](https://github.com/atiqahammed/Bengali-Braille-to-Text-Translator/blob/Documentation/documents/systemVeriable.PNG)

- Check the environment variable has the Jdk and Jre director path. If it not exists then add those in the environment variable path.

![Environment Variable View](https://github.com/atiqahammed/Bengali-Braille-to-Text-Translator/blob/Documentation/documents/env.PNG)

- Install openvc version opencv-4.1.1 and copy the opencv_java411.dll file and add it "C:\Program Files (x86)\Common Files\Oracle\Java\javapath" directory. 


## Installation

Download the exe file from [here](https://github.com/atiqahammed/Bengali-Braille-to-Text-Translator/releases) and install in the system. Installation procedure are given below.

#### Step 1:
Double click on the exe file 

![Step 1](https://github.com/atiqahammed/Bengali-Braille-to-Text-Translator/blob/master/documents/1.PNG)

#### Step 2:
A Installation window will appear. Then double click on the **Next** button to install the application 

![Step 2](https://github.com/atiqahammed/Bengali-Braille-to-Text-Translator/blob/master/documents/2.PNG)

#### Step 3:
After completing the installation click on the **Finish** button to complete the installation

![Step 3](https://github.com/atiqahammed/Bengali-Braille-to-Text-Translator/blob/master/documents/3.PNG)

#### Step 4:
To launch the application Run the application as **Administrator**. The translate braille code image into text.


## User Manual
Braille to Text Translator is a desktop application. The use of this application is given step by step.

### Step 1: Launch the application
After launching the application a window will come. The screenshot is given below.

![Launch the application](https://github.com/atiqahammed/Bengali-Braille-to-Text-Translator/blob/master/documents/4.png)

### Step 2: Go to Translation Page
After clicking on the Translate button the following window will open from where the user can translate a scanned image of braille code.

![Go to Translation Page](https://github.com/atiqahammed/Bengali-Braille-to-Text-Translator/blob/master/documents/5.PNG)

### Step 3: Choosing Image File
Clicking on the Choose image button there a file chooser window will open. Using the file chooser user can choose any image file for translation. If the user wish to provide the file path directly she/he can type the file path in the text box just right to the Choose Image button.

![Choosing Image File](https://github.com/atiqahammed/Bengali-Braille-to-Text-Translator/blob/master/documents/6.PNG)

### Step 4: Image to Text Translation
After selecting user will click on the Translate image button. The corresponding Bengali text will be in the text field below the Translate image button as output.

![Choosing Image File](https://github.com/atiqahammed/Bengali-Braille-to-Text-Translator/blob/master/documents/7.PNG)

### Step 5: Saving the Output
To save the output in file the user will click on the Save Text button. A file chooser will open. The user will get option to select directory and file name to save the output file.

![Choosing Image File](https://github.com/atiqahammed/Bengali-Braille-to-Text-Translator/blob/master/documents/8.PNG)

## Output Result

In this application the main focus was translate image into corresponding Bengali text. Predicting Bengali characters and the predicting the corresponding word from braille code was the main concern.
For developing this project 11 dataset of Bengali braille code has been used. All the dataset is collected from a visual impaired person who is a former student of University of Dhaka. At time of collecting the dataset we consider to cover all the characters to be appeared in the dataset. We also try to cover the complex letter and the Bengali character those takes two braille cells to represent a single Bengali character. Dataset 1 has 12 lines with 188 characters and 35 words. Dataset 2 has 5 lines with 73 characters and 16 words. In total there exists 1745 letters and 353 words in all datasets.
The average accuracy of predicting correct letter is 94.7%. From the predicting letter the average accuracy of predicting word is 83%. All results are represented in the table given below.


|Data File|Line Number          |Number Of Letter|Correctly Identified Letter|Accuracy (%)|Number of Words|Number of Words Where All Characters are Correctly Identified|Word Accuracy|
|---------|---------------------|----------------|---------------------------|------------|---------------|-------------------------------------------------------------|-------------|
|data_01  |1                    |21              |20                         |95.23809524 |3              |1                                                            |33.33333333  |
|data_01  |2                    |19              |18                         |94.73684211 |4              |3                                                            |75           |
|data_01  |3                    |7               |6                          |85.71428571 |1              |0                                                            |0            |
|data_01  |4                    |17              |16                         |94.11764706 |4              |2                                                            |50           |
|data_01  |5                    |17              |17                         |100         |4              |4                                                            |100          |
|data_01  |6                    |22              |21                         |95.45454545 |3              |2                                                            |66.66666667  |
|data_01  |7                    |15              |15                         |100         |3              |3                                                            |100          |
|data_01  |8                    |9               |9                          |100         |2              |2                                                            |100          |
|data_01  |9                    |18              |17                         |94.44444444 |3              |2                                                            |66.66666667  |
|data_01  |10                   |16              |15                         |93.75       |4              |3                                                            |75           |
|data_01  |11                   |18              |17                         |94.44444444 |3              |2                                                            |66.66666667  |
|data_01  |12                   |9               |9                          |100         |1              |1                                                            |100          |
|data_01  |All Lines            |188             |180                        |95.74468085 |35             |25                                                           |71.42857143  |
|data_02  |1                    |14              |14                         |100         |3              |3                                                            |100          |
|data_02  |2                    |16              |16                         |100         |3              |3                                                            |100          |
|data_02  |3                    |15              |15                         |100         |4              |4                                                            |100          |
|data_02  |4                    |10              |10                         |100         |2              |2                                                            |100          |
|data_02  |5                    |18              |18                         |100         |4              |4                                                            |100          |
|data_02  |All Lines            |73              |73                         |100         |16             |16                                                           |100          |
|data_03  |1                    |20              |20                         |100         |3              |3                                                            |100          |
|data_03  |2                    |15              |14                         |93.33333333 |2              |2                                                            |100          |
|data_03  |3                    |18              |17                         |94.44444444 |4              |3                                                            |75           |
|data_03  |4                    |17              |16                         |94.11764706 |4              |3                                                            |75           |
|data_03  |5                    |19              |19                         |100         |3              |3                                                            |100          |
|data_03  |6                    |6               |6                          |100         |2              |2                                                            |100          |
|data_03  |7                    |18              |18                         |100         |4              |4                                                            |100          |
|data_03  |8                    |10              |9                          |90          |2              |1                                                            |50           |
|data_03  |9                    |15              |14                         |93.33333333 |4              |4                                                            |100          |
|data_03  |10                   |22              |19                         |86.36363636 |3              |1                                                            |33.33333333  |
|data_03  |11                   |15              |13                         |86.66666667 |5              |5                                                            |100          |
|data_03  |All Lines            |175             |165                        |94.28571429 |36             |31                                                           |86.11111111  |
|data_04  |1                    |13              |13                         |100         |3              |3                                                            |100          |
|data_04  |2                    |4               |4                          |100         |1              |1                                                            |100          |
|data_04  |3                    |12              |12                         |100         |3              |3                                                            |100          |
|data_04  |4                    |14              |14                         |100         |4              |4                                                            |100          |
|data_04  |5                    |18              |18                         |100         |4              |4                                                            |100          |
|data_04  |6                    |4               |4                          |100         |1              |1                                                            |100          |
|data_04  |7                    |13              |13                         |100         |3              |3                                                            |100          |
|data_04  |8                    |16              |16                         |100         |4              |4                                                            |100          |
|data_04  |9                    |10              |9                          |90          |2              |1                                                            |50           |
|data_04  |All Lines            |104             |103                        |99.03846154 |25             |24                                                           |96           |
|data_05  |1                    |15              |15                         |100         |4              |4                                                            |100          |
|data_05  |2                    |18              |16                         |88.88888889 |4              |3                                                            |75           |
|data_05  |3                    |16              |15                         |93.75       |3              |2                                                            |66.66666667  |
|data_05  |4                    |21              |19                         |90.47619048 |4              |3                                                            |75           |
|data_05  |5                    |17              |16                         |94.11764706 |4              |3                                                            |75           |
|data_05  |6                    |14              |12                         |85.71428571 |3              |1                                                            |33.33333333  |
|data_05  |7                    |14              |14                         |100         |4              |4                                                            |100          |
|data_05  |8                    |5               |5                          |100         |1              |1                                                            |100          |
|data_05  |9                    |15              |13                         |86.66666667 |3              |2                                                            |66.66666667  |
|data_05  |10                   |15              |14                         |93.33333333 |4              |2                                                            |50           |
|data_05  |11                   |13              |12                         |92.30769231 |3              |2                                                            |66.66666667  |
|data_05  |All Lines            |163             |151                        |92.63803681 |37             |27                                                           |72.97297297  |
|data_06  |1                    |11              |10                         |90.90909091 |4              |3                                                            |75           |
|data_06  |2                    |19              |18                         |94.73684211 |4              |4                                                            |100          |
|data_06  |3                    |10              |10                         |100         |2              |2                                                            |100          |
|data_06  |4                    |18              |18                         |100         |4              |4                                                            |100          |
|data_06  |5                    |19              |18                         |94.73684211 |4              |3                                                            |75           |
|data_06  |6                    |16              |16                         |100         |3              |3                                                            |100          |
|data_06  |All Lines            |93              |90                         |96.77419355 |21             |19                                                           |90.47619048  |
|data_07  |1                    |24              |23                         |95.83333333 |4              |2                                                            |50           |
|data_07  |2                    |24              |22                         |91.66666667 |4              |2                                                            |50           |
|data_07  |3                    |24              |23                         |95.83333333 |5              |4                                                            |80           |
|data_07  |4                    |23              |22                         |95.65217391 |5              |4                                                            |80           |
|data_07  |5                    |25              |23                         |92          |5              |3                                                            |60           |
|data_07  |6                    |14              |14                         |100         |3              |3                                                            |100          |
|data_07  |7                    |25              |19                         |76          |5              |3                                                            |60           |
|data_07  |8                    |25              |25                         |100         |5              |5                                                            |100          |
|data_07  |9                    |9               |6                          |66.66666667 |2              |1                                                            |50           |
|data_07  |All Lines            |193             |177                        |91.70984456 |38             |27                                                           |71.05263158  |
|data_08  |1                    |26              |20                         |76.92307692 |4              |2                                                            |50           |
|data_08  |2                    |10              |9                          |90          |2              |2                                                            |100          |
|data_08  |3                    |23              |21                         |91.30434783 |6              |5                                                            |83.33333333  |
|data_08  |4                    |24              |24                         |100         |4              |4                                                            |100          |
|data_08  |5                    |23              |23                         |100         |5              |5                                                            |100          |
|data_08  |6                    |22              |21                         |95.45454545 |5              |4                                                            |80           |
|data_08  |7                    |20              |19                         |95          |4              |3                                                            |75           |
|data_08  |8                    |6               |6                          |100         |1              |1                                                            |100          |
|data_08  |9                    |20              |20                         |100         |5              |5                                                            |100          |
|data_08  |10                   |23              |21                         |91.30434783 |5              |4                                                            |80           |
|data_08  |11                   |7               |7                          |100         |1              |1                                                            |100          |
|data_08  |All Lines            |204             |191                        |93.62745098 |36             |32                                                           |88.88888889  |
|data_09  |1                    |26              |26                         |100         |6              |6                                                            |100          |
|data_09  |2                    |22              |19                         |86.36363636 |6              |5                                                            |83.33333333  |
|data_09  |3                    |10              |10                         |100         |2              |2                                                            |100          |
|data_09  |4                    |21              |19                         |90.47619048 |4              |3                                                            |75           |
|data_09  |5                    |25              |21                         |84          |5              |3                                                            |60           |
|data_09  |6                    |21              |19                         |90.47619048 |5              |3                                                            |60           |
|data_09  |7                    |25              |24                         |96          |4              |3                                                            |75           |
|data_09  |8                    |6               |5                          |83.33333333 |1              |1                                                            |100          |
|data_09  |9                    |20              |17                         |85          |5              |3                                                            |60           |
|data_09  |10                   |19              |17                         |89.47368421 |2              |1                                                            |50           |
|data_09  |All Lines            |195             |177                        |90.76923077 |40             |30                                                           |75           |
|data_10  |1                    |23              |23                         |100         |5              |5                                                            |100          |
|data_10  |2                    |23              |22                         |95.65217391 |4              |3                                                            |75           |
|data_10  |3                    |22              |21                         |95.45454545 |4              |3                                                            |75           |
|data_10  |4                    |25              |25                         |100         |5              |3                                                            |60           |
|data_10  |5                    |11              |11                         |100         |2              |2                                                            |100          |
|data_10  |6                    |21              |21                         |100         |6              |6                                                            |100          |
|data_10  |7                    |11              |11                         |100         |3              |3                                                            |100          |
|data_10  |8                    |21              |20                         |95.23809524 |4              |2                                                            |50           |
|data_10  |9                    |19              |19                         |100         |3              |3                                                            |100          |
|data_10  |All Lines            |176             |173                        |98.29545455 |36             |30                                                           |83.33333333  |
|data_11  |1                    |21              |20                         |95.23809524 |6              |5                                                            |83.33333333  |
|data_11  |2                    |24              |23                         |95.83333333 |4              |3                                                            |75           |
|data_11  |3                    |12              |10                         |83.33333333 |2              |1                                                            |50           |
|data_11  |4                    |21              |21                         |100         |4              |4                                                            |100          |
|data_11  |5                    |12              |12                         |100         |2              |2                                                            |100          |
|data_11  |6                    |25              |25                         |100         |4              |6                                                            |66.66666667  |
|data_11  |7                    |21              |21                         |100         |6              |6                                                            |100          |
|data_11  |8                    |24              |22                         |91.66666667 |4              |2                                                            |50           |
|data_11  |9                    |4               |4                          |100         |1              |1                                                            |100          |
|data_11  |10                   |17              |16                         |94.11764706 |3              |2                                                            |66.66666667  |
|data_11  |All Lines            |181             |174                        |96.13259669 |33             |32                                                           |96.96969697  |
|All data |All lines of all data|1745            |1654                       |94.78510029 |353            |293                                                         |83.00283286  |


It was so much challenging to prepare a well describtion for the first time. I think that this describtion has been written in an easy-to-read way as well as with full information required to have a good concept over the idea. The reader of should easily understand the information of the summary.
