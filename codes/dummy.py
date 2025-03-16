import cv2
import numpy as np
import os
import matplotlib.pyplot as plt


print([a for a in range(0,8)])











"""
images_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\ParsingTests"

for images in os.listdir(images_path):
    
    if images.split(".")[-1] in ["png","jpg","jpeg","webp"]:
        
        image_name = images.split(".")[-2]

        print(image_name)

        image = cv2.imread(r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\ParsingTests"+f"\\{images}")

        save_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\ParsingTests\Tests"+f"\\{image_name}"

        os.makedirs(save_path, exist_ok=True)

        gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
        cv2.imwrite(f"{save_path}\\gray.png", gray)

        blur = cv2.GaussianBlur(gray, (3,3), 0)
        cv2.imwrite(f"{save_path}\\blur.png", blur)

        _, thresh = cv2.threshold(blur, 0, 255, cv2.THRESH_BINARY_INV + cv2.THRESH_OTSU)
        cv2.imwrite(f"{save_path}\\thresh.png", thresh)

        contours, _ = cv2.findContours(thresh, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

        #print(f"Contours is this: {contours}")
        #print(f"A single contour: {contours[0]}")
        #print(f"Its type is: {type(contours[0])}")
        
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

            x, y, w, h = cv2.boundingRect(contour)
            char = thresh[y:y+h, x:x+w]

            char_images.append(char)
            only_contours.append(contour_image)

        for i, char in enumerate(char_images):
            cv2.imwrite(f"{save_path}\{i}_cut.png", char)
            cv2.imwrite(f"{save_path}\{i}_contour.png", only_contours[i])
"""