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
	