import pygame
from pygame.rect import Rect
from GameObject import GameObject
import Core
import os

class MazeBlock(GameObject):
    def __init__(self,wall_state, x, y, w = 100, h=100):
        super().__init__(x,y,w,h)
        self.texture = pygame.image.load(Core.resource_path(os.path.join('images','block.png'))).convert_alpha()
        self.wall_texture = pygame.image.load(Core.resource_path(os.path.join('images','wall.png'))).convert_alpha()
        self.wall_texture = pygame.transform.scale(self.wall_texture,(int(self.wall_texture.get_width()/4),int(self.wall_texture.get_height()/4)))
        self.wall_texture = pygame.transform.rotate(self.wall_texture ,90)
        self.exit_texture = pygame.image.load(Core.resource_path(os.path.join('images','exit.png'))).convert_alpha()
        self.exit_texture = pygame.transform.scale(self.exit_texture,(int(self.exit_texture.get_width()/4),int(self.exit_texture.get_height()/4)))
        self.exit_texture = pygame.transform.rotate(self.exit_texture ,90)
        if wall_state[0] == "obstacle":
                self.texture.blit(self.wall_texture,(0,0))
        if wall_state[1] == "obstacle":
                self.texture.blit(pygame.transform.rotate(self.wall_texture ,-90),(self.texture.get_width() -self.wall_texture.get_height(),0))
        if wall_state[2] == "obstacle":
                self.texture.blit(pygame.transform.rotate(self.wall_texture ,180),(0,self.texture.get_height() -self.wall_texture.get_height()))
        if wall_state[3] == "obstacle":
                self.texture.blit(pygame.transform.rotate(self.wall_texture ,90),(0,0))

        if wall_state[0] == "exit":
                self.texture.blit(self.exit_texture,(0,0))
        if wall_state[1] == "exit":
                self.texture.blit(pygame.transform.rotate(self.exit_texture ,-90),(self.texture.get_width() -self.exit_texture.get_height(),0))
        if wall_state[2] == "exit":
                self.texture.blit(pygame.transform.rotate(self.exit_texture ,180),(0,self.texture.get_height() -self.exit_texture.get_height()))
        if wall_state[3] == "exit":
                self.texture.blit(pygame.transform.rotate(self.exit_texture ,90),(0,0))

    def update(self,time_delta, zoom = 1, camera_offset = (0,0)):
       #self.move(10*time_delta,5*time_delta)
       pass
