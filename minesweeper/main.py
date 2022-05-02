from tkinter import *

WINDOW_WIDTH = 860
WINDOW_HEIGHT = 640

root = Tk()
# Override default setting for the window
root.configure(bg='black')
root.geometry(str(WINDOW_WIDTH)+'x'+str(WINDOW_HEIGHT))
root.title('Minesweeper Game')
root.resizable(False, False)

top_frame = Frame(
    root,
    bg='red',
    width=WINDOW_WIDTH,
    height=WINDOW_HEIGHT/5
)
top_frame.place(x=0, y=0)


# Run the window
root.mainloop()
