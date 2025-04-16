import cv2
import numpy as np
import os
import matplotlib.pyplot as plt


class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

    def Print(self):
        if self.next is not None:
            print(f"{self.val} ",end="")
            self.next.Print()
        else:
            print(f"{self.val}",end="")


class Solution:
    def addTwoNumbers(self, l1, l2):
        number_l1_reverse = ""
        number_l2_reverse = ""
        
        currentList_1 = l1
        while currentList_1 is not None:
            number_l1_reverse += str(currentList_1.val)
            currentList_1 = currentList_1.next

        currentList_2 = l2
        while currentList_2 is not None:
            number_l2_reverse += str(currentList_2.val)
            currentList_2 = currentList_2.next

        number_l1 = number_l1_reverse[::-1]
        number_l2 = number_l2_reverse[::-1]

        new_number = str(int(number_l2) + int(number_l1))


        previousNode = None
        
        for digit in new_number:

            currentNode = ListNode(digit, previousNode)

            previousNode = currentNode
        
        return currentNode


A = 15
print(A//10)