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
        menu_pos = (w/2 - btn_size[0]/2,h/2 - 100)
        btn_marigin = btn_size[1]+10
        self.btn_find_server = pygame_gui.elements.UIButton(
                                            relative_rect=pygame.Rect(menu_pos, btn_size),
                                            text='Find Server',
                                            manager=self.manager)
        self.btn_exit = pygame_gui.elements.UIButton(
                                            relative_rect=pygame.Rect((menu_pos[0],menu_pos[1]+btn_marigin), btn_size),
                                            text='Exit',
                                            manager=self.manager)
        self.btn_pressed_call_backs.append((self.btn_exit,self.btn_exit_pressed))
        self.btn_pressed_call_backs.append((self.btn_find_server,self.btn_find_server_pressed))

    def btn_find_server_pressed(self, event):
        print("find game pressed")

    def btn_exit_pressed(self,event):
        sys.exit()