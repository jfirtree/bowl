# Bowl: Bowling Scorekeeper Command Line App

This is a simple application to compute your score in a ten-pin, ten-frame bowling game.

Provide your rolls as a series of numbers separated by a symbol like a space, comma or semicolon.  
The app is not picky about format, as long as your numbers are separated by a non-numeric character!

The app will compute your score, and print out a frame-by-frame breakdown.

1. Download the latest [release](https://github.com/jfirtree/bowl/archive/1.01.zip).
2. Extract the archive file.
3. Within the extracted folder, run the program according to usage instructions below.

## Usage
The \<rolls\> parameter is a list of each of your rolls in numeric format.
#### Valid Input Examples 
`{10,2,3}`

`10 2 3`

`(10),(2,3)`

### Windows
### Linux
`sh bowl.sh <rolls>`
### Java
`java -jar target/bowl-1.0.jar <rolls>`

## Error-checking
* Verifies that you have inputted the right number of rolls for a ten-frame game.
* Verifies that the pins you report knocking down are within the set of upright pins (<= 10 for first ball, and <= remainder for second ball).
