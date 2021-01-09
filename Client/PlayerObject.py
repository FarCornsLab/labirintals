import pygame
from pygame import surface
from pygame.rect import Rect
from GameObject import GameObject
import Core
import os

class PlayerObject(GameObject):
    def __init__(self, x, y, w, h):
        super().__init__(x,y,w,h)
        self.animation_speed = 5
        self.cur_frame = 0
        self.load_textures()
        self.texture = self.textures[0][0]

    def load_textures(self):
        self.textures = []
        for i in range(4):
            cropped = []
            for j in range(3):
                cropped.append(pygame.Surface((32,32),pygame.SRCALPHA))
                cropped[j].fill((0,0,0,0))
                cropped[j].blit(pygame.image.load(Core.resource_path(os.path.join('images','mouse.png'))),(0,0),
                pygame.Rect((j*32,i*32),(32,32)))
                cropped[j] = cropped[j].convert_alpha()
            self.textures.append(cropped)
    def rotate_texture(self):
        pass

    def update(self,time_delta):
       #self.move(10*time_delta,5*time_delta)
        self.rot += 90*time_delta
        self.cur_frame = self.cur_frame + self.animation_speed * time_delta
        if self.cur_frame > 2:
           self.cur_frame = 0
        self.texture = self.textures[int((self.rot%360)/90)][int(self.cur_frame)]
