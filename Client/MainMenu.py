import pygame
import sys
from collections import defaultdict
from Scene import Scene
import Core
import os
import pygame_gui

class MainMenu(Scene):
    def __init__(self):
        super().__init__(Core.resource_path(os.path.join('images','MainMenu_backgraund.png')))
        self._create_menu()
    def _create_menu(self):
        w,h = pygame.display.get_surface().get_size()
        btn_size = (100,50)
        btn_marigin = btn_size[1]+10
        menu_padding = 50
        panel_size = (btn_size[0]+menu_padding*2,btn_marigin*2 + menu_padding*2)
        self.panel = pygame_gui.elements.UIPanel(pygame.Rect((w/2 - panel_size[0]/2,h/2 -panel_size[1]/2),panel_size),
                                        starting_layer_height=4,
                                        manager=self.ui_manager)
        self.btn_find_server = pygame_gui.elements.UIButton(
                                            relative_rect=pygame.Rect((menu_padding,menu_padding), btn_size),
                                            text='Find Server',
                                            container=self.panel,
                                            manager=self.ui_manager)
        self.btn_exit = pygame_gui.elements.UIButton(
                                            relative_rect=pygame.Rect((menu_padding,menu_padding+btn_marigin), btn_size),
                                            text='Exit',
                                            container=self.panel,
                                            manager=self.ui_manager)
        self.btn_pressed_call_backs.append((self.btn_exit,self.btn_exit_pressed))
        self.btn_pressed_call_backs.append((self.btn_find_server,self.btn_find_server_pressed))

    def btn_find_server_pressed(self, event):
        Core.core.load_scene("FindServerMenu")

    def btn_exit_pressed(self,event):
        sys.exit()