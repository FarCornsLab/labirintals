import pygame
import sys
from collections import defaultdict
import Scene
import os
import json

from MainMenu import MainMenu
from FindServerMenu import FindServerMenu
from NetworkManager import NetworkManager
from WaitStartGameMenu import WaitStartGameMenu

def resource_path(relative):
    relative = os.path.join('resource',relative)
    if hasattr(sys, "_MEIPASS"):
        return os.path.join(sys._MEIPASS, relative)
    return os.path.join(relative)

class Core:
    def __init__(self):
        with open(os.path.join('configs',"config.json"), "r") as read_file:
            self.config = json.load(read_file)
        pygame.mixer.init(44100, -16, 2, 4096)
        pygame.init()
        pygame.font.init()
        self.surface = pygame.display.set_mode(self.config['resolution'])
        pygame.display.set_caption('labirintlas')
        self.cur_scene:Scene = None
        self.next_scene:Scene = None
        print("Core inited")
        self.scenes_pull = {"MainMenu":MainMenu,"FindServerMenu":FindServerMenu,"WaitStartGameMenu":WaitStartGameMenu}
        self.net_manager = NetworkManager(self.config["player_name"])
    def load_scene(self,scene_str:str):
        if self.cur_scene == None:
            self.cur_scene = self.scenes_pull[scene_str]()
        else:
            self.next_scene = self.scenes_pull[scene_str]()
            self.cur_scene.exit()
    def update_config(self):
        with open(os.path.join('configs',"config.json"), "w") as read_file:
            json.dump(self.config,read_file)
        with open(os.path.join('configs',"config.json"), "r") as read_file:
            self.config = json.load(read_file)
    def run_game(self):
        while True:
            self.cur_scene.run()
            self.cur_scene = self.next_scene



core = Core()
