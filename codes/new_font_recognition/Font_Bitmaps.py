from Recognition_Classes import * 

#uppercase_extraction = Extractor(r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\uppercase\characters\all_characters.jpg")
#lowercase_extraction = Extractor(r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\lowercase\characters\all_characters.jpg")
#numbers_extraction = Extractor(r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\numbers\characters\all_characters.jpg")

#uppercase_extraction.SaveExtraction()
#lowercase_extraction.SaveExtraction()
#numbers_extraction.SaveExtraction() 


uppercase_characters_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\uppercase\characters\Images"
lowercase_characters_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\lowercase\characters\Images"
numbers_characters_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\numbers\characters\Images"

lowercase_square_image_save_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\lowercase\squared_images"
uppercase_square_images_save_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\uppercase\squared_images"
numbers_square_images_save_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\numbers\squared_images"

lowercase_bitmaps_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\lowercase\bitmaps\arial_lowercase.json" 
uppercase_bitmaps_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\uppercase\bitmaps\arial_uppercase.json" 
nuumbers_bitmaps_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\numbers\bitmaps\arial_numbers.json" 

#lowercase_bitmap_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\lowercase\squared_images"
#uppercase_bitmap_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\uppercase\squared_images"
#numbers_bitmap_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\new_font_recognition\numbers\squared_images"


pure_character_images = []

for image_name_jpg in os.listdir(uppercase_characters_path):
    image_path = os.path.join(uppercase_characters_path,image_name_jpg)
    image_name = image_name_jpg.split(".")[0]
    image = cv2.imread(image_path,cv2.IMREAD_GRAYSCALE)

    pure_character_images.append([image,(0,0),image_name])

SQUARER = Squarer(pure_character_images)


uppercase_bitmaps = FontBitMaps(SQUARER.characterSquares,5)
uppercase_bitmaps.SaveBitmaps(uppercase_bitmaps_path)


pure_character_images = []

for image_name_jpg in os.listdir(lowercase_characters_path):
    image_path = os.path.join(lowercase_characters_path,image_name_jpg)
    image_name = image_name_jpg.split(".")[0]
    image = cv2.imread(image_path,cv2.IMREAD_GRAYSCALE)

    pure_character_images.append([image,(0,0),image_name])

SQUARER = Squarer(pure_character_images)


lowercase_bitmaps = FontBitMaps(SQUARER.characterSquares,5)
lowercase_bitmaps.SaveBitmaps(lowercase_bitmaps_path)

uppercase_bit_size = 5
lowercase_bit_size = 6  #Based on trials
