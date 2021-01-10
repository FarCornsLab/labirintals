import pygame
import sys
import pygame_gui
from collections import defaultdict


class Scene:
    def __init__(self,back_image):
        self.background_image =  back_image
        self.background_image = pygame.transform.scale(self.background_image, pygame.display.get_surface().get_size())
        self.frame_rate = 30
        self.exit_flag = False
        self.objects = []
        self.btn_pressed_call_backs = []
        self.clock = pygame.time.Clock()
        self.keydown_handlers = defaultdict(list)
        self.keyup_handlers = defaultdict(list)
        self.mouse_handlers = []
        self.custom_event_handlers = defaultdict(list)
        self.ui_manager = pygame_gui.UIManager(pygame.display.get_surface().get_size())
        self.surface = pygame.display.get_surface()
        self.zoom = 1
        self.camera_offset = (0,0)
        self.key_state = defaultdict(bool)

    def screen_point_to_global(self,point):
        return ((point[0] - self.camera_offset[0])/self.zoom,(point[1] - self.camera_offset[1])/self.zoom)

    def update(self):
        for o in self.objects:
            o.update(self.time_delta,self.zoom,self.camera_offset)

    def draw(self):
        for o in self.objects:
            o.draw(self.surface,self.zoom,self.camera_offset)

    def handle_events(self):
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                sys.exit()
            elif event.type == pygame.KEYDOWN:
                for handler in self.keydown_handlers[event.key]:
                    handler(event.key)
            elif event.type == pygame.KEYUP:
                for handler in self.keyup_handlers[event.key]:
                    handler(event.key)
            elif event.type in self.custom_event_handlers:
                for handler in self.custom_event_handlers[event.type]:
                    handler(event)
            elif event.type == pygame.USEREVENT and event.user_type == pygame_gui.UI_BUTTON_PRESSED:
                for cb in self.btn_pressed_call_backs:
                    if(cb[0] == event.ui_element):
                        cb[1](event)
            elif event.type == pygame.USEREVENT and event.user_type == pygame_gui.UI_TEXT_ENTRY_FINISHED:
                print("text: "+ event.text)
            self.ui_manager.process_events(event)
    def exit(self):
        self.exit_flag = True
    def run(self):
        self.exit_flag = False
        while not self.exit_flag:
            self.time_delta = self.clock.tick(self.frame_rate)/1000.0
            self.surface.blit(self.background_image, (0, 0))
            self.handle_events()
            self.update()
            self.draw()
            self.ui_manager.update(self.time_delta)
            self.ui_manager.draw_ui(self.surface)
            pygame.display.update()
            
