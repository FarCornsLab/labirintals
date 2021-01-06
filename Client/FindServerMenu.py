import pygame
import sys
from collections import defaultdict
from Scene import Scene
import Core
import os
import pygame_gui

class FindServerMenu(Scene):
    def __init__(self):
        super().__init__(Core.resource_path(os.path.join('images','MainMenu_backgraund.png')))
        self._create_menu()

    def _create_menu(self):
        w,h = pygame.display.get_surface().get_size()
        panel_size = (600,400)
        self.panel = pygame_gui.elements.UIPanel(pygame.Rect((w/2 - panel_size[0]/2,h/2 -panel_size[1]/2),panel_size),
                                        starting_layer_height=4,
                                        manager=self.ui_manager)
        self.btn_back = pygame_gui.elements.UIButton(
                                            relative_rect=pygame.Rect((5,5),(50,25)),
                                            text='Back',
                                            container=self.panel,
                                            manager=self.ui_manager)
        self.btn_pressed_call_backs.append((self.btn_back,self.btn_back_pressed))
        self.btn_connect = pygame_gui.elements.UIButton(
                                            relative_rect=pygame.Rect((50,350),(100,25)),
                                            text='Connect',
                                            container=self.panel,
                                            manager=self.ui_manager)
        self.btn_pressed_call_backs.append((self.btn_connect,self.btn_connect_pressed))
        self.server_list = pygame_gui.elements.UISelectionList(pygame.Rect(50, 50, 500, 300),
                        item_list=['Server 1',
                                   'Server 2',
                                   'Server 3',
                                   'Server 4',
                                   'Server 5',
                                   'Server 6',
                                   'Server 7',
                                   'Server 8',
                                   'Server 9',
                                   'Server 10',
                                   'Server 11',
                                   'Server 12',
                                   'Server 13',
                                   'Server 14',
                                   'Server 15',
                                   'Server 16',
                                   'Server 17',
                                   'Server 18',
                                   'Server 19',
                                   'Server 20'
                                   ],
                        manager=self.ui_manager,
                        container=self.panel)
    def btn_back_pressed(self, event):
        Core.core.load_scene("MainMenu")
    def btn_connect_pressed(self, event):
        if self.server_list.get_single_selection() != None:
            print("Connect to "+ self.server_list.get_single_selection() )