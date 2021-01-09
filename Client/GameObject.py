import pygame
from pygame.rect import Rect


class GameObject:
    def __init__(self, x, y, w, h, speed=(0,0)):
        self.position = (x,y)
        self.size = (w,h)
        self.speed = speed
        self.rot = 0
        self.is_texture_rotated = False
        self.texture = None

    def screen_rect_to_global(rect,zoom, camera_offet):
        return pygame.Rect(((rect.topleft[0] + camera_offet[0])/zoom,(rect.topleft[1] + camera_offet[1])/zoom ),
                            (rect.size[0] / zoom ,rect.size[0] / zoom))

    def global_rect_to_screen(rect,zoom, camera_offet):

        return pygame.Rect((rect.topleft[0] * zoom + camera_offet[0],rect.topleft[1] * zoom + camera_offet[1] ),
                            (rect.size[0] * zoom ,rect.size[0] * zoom))

    def rotate_texture(self):
        if self.rot != 0:
            self.draw_texture = pygame.transform.rotate(self.texture,self.rot)

    def draw(self, surface, zoom = 1, camera_offet = (0,0)):
        self.rotate_texture()
        self.draw_texture = pygame.transform.scale(self.texture,self.size)
        surface.blit( self.draw_texture,pygame.Rect(self.position,self.size))

    def move(self, dx, dy):
        self.position = (self.position[0]+dx,self.position[1]+dy)

    def update(self,time_delta):
        pass
