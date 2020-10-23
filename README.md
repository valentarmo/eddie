# Eddie
Eddie is a very simple terminal based text editor written in java that behaves a
bit like vi. Currently the editor supports almost every feature I wanted to implement,
but if you look at the code you'll see it needs a lot of refactoring (I apologize
about that), and of course tests (apologies again). The text rendering algorithm
needs some improvement as well, though is not as critical. Currently, due to time
limitations I've stopped working on this project.

## Usage
Eddie is built using maven, so if you want try it, first clone the repository i.e. 
`git clone https://github.com/valentarmo/eddie`. Then, the easiest way to use it is by
generating a fat JAR, to do this, simply go to the project's root directory and type
`mvn package`. You'll find the JAR under the `target` directory, to run it type
`java -jar eddie.jar` or double click on the file.

Eddie has three modes, normal, insert and visual.
* ### Normal Mode
    Normal is the default mode. From this mode the user can move around the file,
    go into either insert or visual mode, or execute commands.

    #### Current Normal Mode Keys
        l - Move right
        h - Move left
        k - Move up
        j - Move down
        i - Enter insert mode
        I - Move to the start of the line and enter insert mode
        a - Move right and enter insert mode
        A - Move to the end of the line and enter insert mode
        o - Insert a new line and enter insert mode
        O - Insert a new line above and enter insert mode
        w - Move to the start of the next word
        e - Move to the end of the current or next word
        b - Move to the start of the current or previous word
        x - delete
        X - Backspace
        0 - Go to the start of the line
        $ - Go to the end of the line
        v - Go into visual mode
        p - Paste 
        : - Type a command. Valid commands are, save, open, and quit, which do as they suggest. Press ESC to cancel. Press Enter to confirm the command and continue with the operation.
    #### Current Visual Mode Keys
        l, h, k, j, 0, $ - Same as in normal mode
        d - cut region and exit to normal mode
        y - copy region and exit to normal mode
    #### Insert Mode
        Every character will be inserted into the text
    #### Keys common to every mode
        ESC - Enter normal mode
        Right arrow - Move right
        Left arrow - Move left
        Up arrow - Move up
        Down arrow - Move down
        Home - Go to start of the line
        End - Go to the end of the line
