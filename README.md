This project is the server side that will respond to requests from clients for POUI's. 
Initial develop will focus solely on serving up images and text instructions, and areas 
for future development include build tracking/timing, traveller tag replacement and 
enforcement of inspections


Purpose:
This project serves as the server backend in a digital work instruction system. When 
requested by a client, it will send images to be displayed client-side, and it can also
enforce inspections (employee number verification to move on to next step, if desired) as
well as receiving and storing timing information reported from clients at the end of a 
build.

Installation:
[**Add instructions on compiling jar via terminal here**]

Usage:
The server server interface is terminal based, and should be started from a terminal to
begin. On startup, it will prompt for the folder containing all images, and after that a 
port number to listen for connections on. Once started, it will display options to pause, 
restart, and end the server. Future development will add in analyzing the results that are
currently just stored when received from client programs. Besides those options, the 
server is meant to run with minimal intervention and is relatively stable.

Storage:
The file structure for storing images is pretty basic, but important. The structure goes
like so:
	Parent Folder -> Assembly Image Folders -> Individual images.
The names that will be displayed client-side as available assemblies are actually the folder names that have images within them. Thus their accurate naming is very important.
The times that are reported from the clients to the server are stored in a timings folder, hidden by default (it’s folder name is .timings). The structure of this folder goes like so:
	Parent Folder -> .timings -> Assembly Name -> Individual files by date.
Within these individual files are the raw times reported from the clients, separated by build.

Assembly Image Folder Structure:
The individual images within the assembly image folder should be named step1.jpg, step2.jpg,…,stepN.jpg, where N is the last step to be displayed client-side. There is one other file that can be within this folder, called inspections.txt that dictates what steps require inspection, and which do not. By default, if this file is not included, no steps will have inspection enforced/server validated.

Inspection.txt Format:
The format of inspection.txt goes as follows. Each line represents a step. Thus the first line corresponds to step1.jpg, the second step2.jpg, etc. If the line contains “no”, it dictates that no inspection is required. However, to make an inspection mandatory, the format is “yes” followed by a semicolon. Following the semicolon is the employee number(s) of those verified to do the inspection. If there are multiple employees allowed to inspect, these number should be separated by semicolons (ie. 123;456;789). No trailing semicolon is necessary.