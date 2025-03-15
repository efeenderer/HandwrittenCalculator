import os
import numpy as np
import cv2 
import time as t

class ImageSquarer:
    def __init__(self, Images):
        pass

    
class LetterExtractor:

    def __init__(self, text_image):
        self.img = text_image
        self.Letters = []
        pass

    def ContourExtractor(self):
        
        gray = cv2.cvtColor(self.img,cv2.COLOR_BGR2GRAY)
        height, width = gray.shape[:2]

        print(f"Height: {height}   Width: {width}")

        _,thresh = cv2.threshold(gray, 0, 255, cv2.THRESH_BINARY_INV + cv2.THRESH_OTSU) 
        
        contours, _ = cv2.findContours(thresh, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_NONE)

        
        for contour in contours:
            for point in contour:
                x, y = point[0]

            x, y, w, h = cv2.boundingRect(contour)
            
            if w<10 or h<10: #If it's this small, it probably is just a noise
                continue

            img = thresh[y:y+h, x:x+w]

            self.Letters.append(img)