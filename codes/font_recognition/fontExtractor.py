import os
import numpy as np
import cv2 as cv
import time as t


fonts_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\font_recognition\fonts"

for font_name in os.listdir(fonts_path):
    font_path = os.path.join(fonts_path,font_name)

    all_chars = os.path.join(font_path,"all_chars.png")
    all_chars = cv.imread(all_chars)
    
    cv.waitKey(0)
    

    gray = cv.cvtColor(all_chars,cv.COLOR_BGR2GRAY)
    height, width = gray.shape[:2]

    print(f"Height: {height}   Width: {width}")

    #cv.imshow("gray",gray)

    _,thresh = cv.threshold(gray, 0, 255, cv.THRESH_BINARY_INV + cv.THRESH_OTSU) 
    cv.waitKey(0) 
    
    #cv.imshow("edged",edged)

    contours, _ = cv.findContours(thresh, cv.RETR_EXTERNAL, cv.CHAIN_APPROX_NONE)

   

    #cv.imshow("Contours",all_chars)
    cv.waitKey(0) 
    cv.destroyAllWindows() 

    """
    for i, contour in enumerate(contours):
        x, y, h, w = cv.boundingRect(contour)
        char_image = all_chars[y:y+h , x:x+w]

        cv.imwrite(filename=font_path + f"\\{i}.png",img=char_image)
        """
    char_images = []
    only_contours = []
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

        x, y, w, h = cv.boundingRect(contour)
        char = thresh[y:y+h, x:x+w]

        char_images.append(char)
        only_contours.append(contour_image)

    for i, char in enumerate(char_images):
        cv.imwrite(f"{font_path}\{i}_cut.png", char)
        cv.imwrite(f"{font_path}\{i}_contour.png", only_contours[i])