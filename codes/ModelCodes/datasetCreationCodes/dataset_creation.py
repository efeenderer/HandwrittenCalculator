import os
import cv2
import numpy as np





letter_pages_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\dataset\annem\Annem"

for index, letter_page in enumerate(os.listdir(letter_pages_path)):
    letter_path = os.path.join(letter_pages_path, letter_page)

    page = cv2.imread(letter_path, cv2.IMREAD_GRAYSCALE)

    blur = cv2.GaussianBlur(page,(3,3),1)

    _, thresh = cv2.threshold(blur, 0, 255, cv2.THRESH_BINARY_INV + cv2.THRESH_OTSU)

    kernel = np.ones((2,2), np.uint8)

    dilation = cv2.dilate(thresh, kernel, iterations=1)


    contours, _ = cv2.findContours(dilation, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

    

    

    if index < 38:   continue
    for contour in contours:
        x, y, w, h = cv2.boundingRect(contour)

        max_edge = max(h,w)
        
        blank = np.zeros((max_edge,max_edge),np.uint8)

        x1,x2 = int( (max_edge - h) / 2 ), int( (max_edge + h) / 2 )
        y1,y2 = int( (max_edge - w) / 2 ), int( (max_edge + w) / 2 )

        blank[x1:x2,y1:y2] = dilation[y:y+h, x:x+w]
        letter = cv2.resize(blank, (64,64))
        

        cv2.namedWindow("lala", cv2.WINDOW_FREERATIO)
        cv2.imshow("lala", letter)
        cv2.waitKey(0)


