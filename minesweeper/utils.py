import settings

def height_prct(percentage):
    return (settings.WINDOW_HEIGHT / 100) * percentage

def width_prct(percentage):
    return (settings.WINDOW_WIDTH / 100) * percentage
