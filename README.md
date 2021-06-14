# Eddie
A simple terminal text editor.

## Build
`mvn package` will create a fat jar at `target/eddie.jar`.

## Usage
Eddie has three modes, normal, insert and visual.
* ### Normal Mode
    Normal is the default mode. From this mode the user can move around the file,
    go into insert, visual mode, or execute commands.

    #### Normal Mode Keys
        l - Move right
        h - Move left
        k - Move up
        j - Move down
        i - Enter insert mode
        w - Move to the start of the next word
        e - Move to the end of the current or next word
        b - Move to the start of the current or previous word
        x - delete
        v - Go into visual mode
        : - Type a command. Valid commands are, save, saveas, open, and quit, which do as they suggest. Press ESC to cancel. Press Enter to submit.
    #### Visual Mode Keys
        l, h, k, j, w, e, b - Same as in normal mode
        x - cut region and exit to normal mode
    #### Insert Mode
        Every character will be inserted into the text
    #### Keys common to every mode
        ESC - Enter normal mode
        Right arrow - Move right
        Left arrow - Move left
        Up arrow - Move up
        Down arrow - Move down
