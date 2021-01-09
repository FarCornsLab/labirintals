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
        self.color = None

    def get_center(self):
        return (self.position[0]+self.size[0]/2,self.position[1]+self.size[1]/2)
    
    def set_center(self,val):
        self.position = (val[0]-self.size[0]/2,val[1]-self.size[1]/2)

    def screen_rect_to_global(rect,zoom, camera_offet):
        return pygame.Rect(((rect.topleft[0] + camera_offet[0])/zoom,(rect.topleft[1] + camera_offet[1])/zoom ),
                            (rect.size[0] / zoom ,rect.size[0] / zoom))

    def global_to_screen(self,zoom,camera_offset):
        return ((self.position[0] * zoom + camera_offset[0],self.position[1] * zoom + camera_offset[1]),
                            (self.size[0] * zoom ,self.size[0] * zoom))

    def rotate_texture(self):
        if self.rot != 0:
            self.draw_texture = pygame.transform.rotate(self.draw_texture,self.rot)
            
    def colorize_texture(self):
        if self.color != None:
            colorImage = pygame.Surface(self.draw_texture.get_size()).convert_alpha()
            colorImage.fill(self.color)
            self.draw_texture.blit(colorImage, (0,0), special_flags = pygame.BLEND_RGBA_MULT)

    def draw(self, surface, zoom = 1, camera_offset = (0,0)):
        self.draw_texture =  pygame.Surface(self.texture.get_size(),pygame.SRCALPHA).convert_alpha()
        self.draw_texture.fill((0,0,0,0))
        self.draw_texture.blit(self.texture, (0,0))
        self.rotate_texture()
        self.colorize_texture()
        screen_pos, screen_size = self.global_to_screen(zoom,camera_offset) 
        self.draw_texture = pygame.transform.scale(self.draw_texture,(int(screen_size[0]),int(screen_size[1])))
        surface.blit( self.draw_texture,pygame.Rect(screen_pos,screen_size))

    def move(self, dx, dy):
        self.position = (self.position[0]+dx,self.position[1]+dy)

    def colidepoint(self,point):
        return Rect(self.position,self.size).collidepoint(point)

    def colide_mouse(self, zoom = 1, camera_offset = (0,0)):
        screen_pos, screen_size = self.global_to_screen(zoom,camera_offset)
        return Rect(screen_pos,screen_size).collidepoint(pygame.mouse.get_pos())

    def update(self,time_delta, zoom = 1, camera_offset = (0,0)):
        pass
