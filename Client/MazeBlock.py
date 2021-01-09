import pygame
from pygame.rect import Rect
from GameObject import GameObject
import Core
import os

class MazeBlock(GameObject):
    def __init__(self, x, y, w, h):
        super().__init__(x,y,w,h)
        self.texture = pygame.image.load(Core.resource_path(os.path.join('images','block.png'))).convert_alpha()

    def update(self,time_delta):
       self.move(10*time_delta,5*time_delta)
