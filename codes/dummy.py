import cv2
import numpy as np
import os

images_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\ParsingTests"

for images in os.listdir(images_path):
    
    if images.split(".")[-1] in ["png","jpg","jpeg","webp"]:
        
        image_name = images.split(".")[-2]

        print(image_name)

        image = cv2.imread(r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\ParsingTests"+f"\\{images}")

        save_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\ParsingTests\Tests"+f"\\{image_name}"

        if not os.path.exists(save_path):
            os.mkdir(save_path)


        gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
        cv2.imwrite(f"{save_path}\\gray.png", gray)

        blur = cv2.GaussianBlur(gray, (5,5), 0)
        cv2.imwrite(f"{save_path}\\blur.png", blur)

        _, thresh = cv2.threshold(blur, 0, 255, cv2.THRESH_BINARY_INV + cv2.THRESH_OTSU)
        cv2.imwrite(f"{save_path}\\thresh.png", thresh)

        contours, _ = cv2.findContours(thresh, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

        char_images = []
        for contour in contours:
            x, y, w, h = cv2.boundingRect(contour)
            char = thresh[y:y+h, x:x+w]
            char_images.append(char)


        for i, char in enumerate(char_images):
            cv2.imwrite(f"{save_path}\{i}.png", char)
