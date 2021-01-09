import pygame
import sys
import random
import time
from collections import defaultdict

from pygame_gui import ui_manager
from Scene import Scene
import Core
import os
import pygame_gui
import pygame

class GameScene(Scene):
    def __init__(self):
        super().__init__(Core.resource_path(os.path.join('images','MainMenu_backgraund.png')))
        self._create_menu()

    def _create_menu(self):
        w,h = pygame.display.get_surface().get_size()
        panel_size = (600,400)
        self.panel = pygame_gui.elements.UIPanel(pygame.Rect((w/2 - panel_size[0]/2,h/2 -panel_size[1]/2),panel_size),
                                        starting_layer_height=4,
                                        manager=self.ui_manager)
        self.btn_disconect = pygame_gui.elements.UIButton(
                                            relative_rect=pygame.Rect((5,5),(100,25)),
                                            text='Disconnect',
                                            container=self.panel,
                                            manager=self.ui_manager)
        self.btn_pressed_call_backs.append((self.btn_disconect,self.btn_disconect_pressed))

    def btn_disconect_pressed(self, event):
        Core.core.net_manager.disconnect()
        Core.core.load_scene("FindServerMenu")
