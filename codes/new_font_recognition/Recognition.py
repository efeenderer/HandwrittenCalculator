import os
import numpy as np
import cv2 
import time as t



class Extractor:
    def __init__(self, image_path):
        self.path = os.path.dirname(image_path)

        self.img = cv2.imread(image_path,cv2.IMREAD_GRAYSCALE)
        self.characterImages = list()

        self.Extraction()
    
    def Extraction(self):

        _,thresh_img = cv2.threshold(self.img, 0,255, cv2.THRESH_BINARY_INV + cv2.THRESH_OTSU)

        contours , _ = cv2.findContours(thresh_img,cv2.RETR_EXTERNAL,cv2.CHAIN_APPROX_NONE)

        self.characterContours = list()

        for contour in contours:
            min_x = min(point[0][0] for point in contour)
            max_x = max(point[0][0] for point in contour)
            min_y = min(point[0][1] for point in contour)
            max_y = max(point[0][1] for point in contour)

            contour_image_width = max_x - min_x + 1
            contour_image_height = max_y - min_y + 1

            contour_image = np.zeros((contour_image_height, contour_image_width), dtype=np.uint8)
            
            for point in contour:
                x, y = point[0]
                contour_image[y - min_y, x - min_x] = 255

            x, y, w, h = cv2.boundingRect(contour)
            char = thresh_img[y:y+h, x:x+w]

            contour_image = cv2.cvtColor(contour_image,cv2.COLOR_GRAY2BGR)
            char = cv2.cvtColor(char,cv2.COLOR_GRAY2BGR)

            self.characterContours.append([contour_image,(x,y)])
            self.characterImages.append([char,(x,y)])
        

    def ShowContours(self):
        for image,coorditanions in self.characterImages:
            cv2.imshow("lala",image)
            cv2.waitKey(0)

    def SaveExtraction(self):
        save_path = os.path.join( self.path , "Images")
        os.makedirs(save_path,exist_ok=True)

        for i, img in enumerate(self.characterImages):
            image = img[0]
            try:
                cv2.imwrite(f"{save_path}\\{i}.jpg",image)
            except Exception as e:
                print(f"There was an error saving number {i} :::: {e}")
"""    
#Since all the characters are extracted, There are no need for the codes below. In fact, if they would run again, it would mess up everything.
#The Extraction class will be used later in the recognition part.



uppercase_extraction = Extractor(r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\uppercase\characters\all_characters.jpg")
lowercase_extraction = Extractor(r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\lowercase\characters\all_characters.jpg")
#number_extraction = Extractor()

uppercase_extraction.SaveExtraction()
lowercase_extraction.SaveExtraction()
#number_extraction.SaveExtraction()


"""

class Squarer:

    def __init__(self):
        pass



