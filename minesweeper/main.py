from tkinter import *
import settings

root = Tk()
# Override default setting for the window
root.configure(bg='black')
#root.geometry(f'{settings.WINDOW_WIDTH}x{settings.WINDOW_HEIGHT}')
root.geometry(str(settings.WINDOW_WIDTH)+'x'+str(settings.WINDOW_HEIGHT))
root.title('Minesweeper Game')
root.resizable(False, False)

top_frame = Frame(
    root,
    bg='red',
    width=settings.WINDOW_WIDTH,
    height=settings.WINDOW_HEIGHT/5
)
top_frame.place(x=0, y=0)

left_frame = Frame(
    root,
    bg='blue',
    width=settings.WINDOW_WIDTH/5,
    height=settings.WINDOW_HEIGHT-settings.WINDOW_HEIGHT/5
)
left_frame.place(x=0, y=settings.WINDOW_HEIGHT/5)

# Run the window
root.mainloop()
