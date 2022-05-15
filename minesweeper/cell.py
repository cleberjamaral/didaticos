from tkinter import Button, Label
from tkinter import messagebox
import tkinter.font as font
import random
import settings
import sys

class Cell:
    all = []
    cell_count = settings.CELLS_COUNT
    cell_count_label_object = None
    btn_face_color = None
    def __init__(self, x, y, is_mine=False):
        self.is_mine = is_mine
        self.is_oneped = False
        self.is_mine_candidate = False
        self.cell_btn_object = None
        self.x = x
        self.y = y

        # Append objects to Cell.all
        Cell.all.append(self)
        
    def create_btn_object(self, location):
        btn = Button(
            location,
            font=font.Font(weight="bold", size=16),
            width=6,
            height=2,
        )
        btn.bind('<Button-1>', self.left_click_actions)
        btn.bind('<Button-3>', self.right_click_actions)
        self.btn_face_color = btn.cget("background")
        self.cell_btn_object = btn

    @staticmethod
    def create_cell_count_label(location):
        lbl = Label(
            location,
            bg='black',
            fg='white',
            text=f"Cells Left: {Cell.cell_count}",
            font=("", 30)
        )
        Cell.cell_count_label_object = lbl

    def left_click_actions(self, event):
        if self.is_mine:
            self.show_mine()
        else:
            if self.surrounded_cells_mines_length == 0:
                for cell in self.surrounded_cells:
                    cell.show_cell()
            self.show_cell()
            if Cell.cell_count == settings.MINES_COUNT:
                messagebox.showinfo(
                    "You won!",
                    "Congratulations, you've found all mines!"
                )


    def get_cell_by_axis(self, x, y):
        for cell in Cell.all:
            if cell.x == x and cell.y == y:
                return cell

    @property
    def surrounded_cells(self):
        cells = [
            self.get_cell_by_axis(self.x - 1, self.y - 1),
            self.get_cell_by_axis(self.x - 1, self.y),
            self.get_cell_by_axis(self.x - 1, self.y + 1),
            self.get_cell_by_axis(self.x, self.y - 1),
            self.get_cell_by_axis(self.x + 1, self.y - 1),
            self.get_cell_by_axis(self.x + 1, self.y),
            self.get_cell_by_axis(self.x + 1, self.y + 1),
            self.get_cell_by_axis(self.x, self.y + 1)
        ]
        cells = [cell for cell in cells if cell is not None]
        return cells

    @property
    def surrounded_cells_mines_length(self):
        counter = 0
        for cell in self.surrounded_cells:
            if cell.is_mine:
                counter += 1
        return counter

    def show_cell(self):
        if not self.is_oneped:
            Cell.cell_count -= 1
            self.cell_btn_object.configure(
                text=f'{self.surrounded_cells_mines_length}',
                bg=self.btn_face_color
            )
            if Cell.cell_count_label_object:
                Cell.cell_count_label_object.configure(
                text=f"Cells Left: {Cell.cell_count}"
            )
        self.is_oneped = True
        self.cell_btn_object.unbind('<Button-1>')
        self.cell_btn_object.unbind('<Button-3>')


    def show_mine(self):
        self.cell_btn_object.configure(bg="red")
        messagebox.showinfo(
            "Game over!",
            "You click on a mine!"
        )
        sys.exit()

    def right_click_actions(self, event):
        if not self.is_mine_candidate:
            self.cell_btn_object.configure(bg="orange")
            self.is_mine_candidate = True
        else:
            self.cell_btn_object.configure(bg=self.btn_face_color)
            self.is_mine_candidate = False

    @staticmethod
    def randomize_mines():
        picked_cells = random.sample(
            Cell.all,
            settings.MINES_COUNT
        )
        for picked_cell in picked_cells:
            picked_cell.is_mine = True

    def __repr__(self):
        return f"Cell({self.x},{self.y})"