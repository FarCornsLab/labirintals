import pygame
import sys
from collections import defaultdict
import Scene
import os
import json

def resource_path(relative):
    relative = os.path.join('resource',relative)
    if hasattr(sys, "_MEIPASS"):
        return os.path.join(sys._MEIPASS, relative)
    return os.path.join(relative)

class Core:
    def __init__(self):
        with open("config.json", "r") as read_file:
            self.config = json.load(read_file)
        pygame.mixer.init(44100, -16, 2, 4096)
        pygame.init()
        pygame.font.init()
        self.surface = pygame.display.set_mode(self.config['resolution'])
        pygame.display.set_caption('labirintlas')
        self.cur_scene:Scene = None
        print("Core inited")
    def deactivate_cur_scene(self):
        if(self.cur_scene !=None):
            self.cur_scene.exit()
    def activate_scene(self,scene:Scene):
        scene.run()


core = Core()
