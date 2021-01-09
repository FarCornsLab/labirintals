import pygame
from pygame import surface
from pygame.rect import Rect
from GameObject import GameObject
import Core
import os

class AimObject(GameObject):
    def __init__(self, x, y, w = 100, h =100,name = "up"):
        super().__init__(x,y,w,h)
        self.animation_speed = 60
        self.animation_speed_cur = self.animation_speed
        self.texture = pygame.image.load(Core.resource_path(os.path.join('images','aim.png'))).convert_alpha()
        self.name = name
        self.picked = False


    def update(self,time_delta, zoom = 1, camera_offset = (0,0)):
        if self.picked:
            self.color = (0,255,0)
        else:
            if self.colide_mouse(zoom,camera_offset):
                self.color = (250,0,0)
            else:
                self.color = (255,255,255)
        
        delta_size = self.animation_speed_cur*time_delta
        self.size = (self.size[0] + delta_size,self.size[1] + delta_size)
        self.position = (self.position[0] - (delta_size/2),self.position[1] - (delta_size/2))
        if self.size[0] >= 100: 
            self.animation_speed_cur = -self.animation_speed
        if self.size[0] < 70: 
            self.animation_speed_cur = self.animation_speed
