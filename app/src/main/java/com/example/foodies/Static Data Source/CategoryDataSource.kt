package com.example.foodies.Static

import com.example.foodies.Models.CategoryThumbModel


class CategoryDataSource {
    companion object{
        val categoryList=ArrayList<CategoryThumbModel>(1)
        fun categoriesInitialiser(): ArrayList<CategoryThumbModel> {
            categoryList.add(CategoryThumbModel("Indian","https://firebasestorage.googleapis.com/v0/b/food-clone-c7e62.appspot.com/o/categories%2Findian_food_image.png?alt=media&token=102b62b5-11bf-49b9-a5c2-bc6c920e7317"))
            categoryList.add(CategoryThumbModel("Indonasian","https://firebasestorage.googleapis.com/v0/b/food-clone-c7e62.appspot.com/o/categories%2Findonasian_food_image.jpg?alt=media&token=66a5cc5e-16d6-4342-a382-e8f07b452b6f"))
            categoryList.add(CategoryThumbModel("Italian","https://firebasestorage.googleapis.com/v0/b/food-clone-c7e62.appspot.com/o/categories%2Fitalian_food_image.png?alt=media&token=47c6f362-78fc-4cb4-8e04-fd3609348fbb"))
            categoryList.add(CategoryThumbModel("Mexican","https://firebasestorage.googleapis.com/v0/b/food-clone-c7e62.appspot.com/o/categories%2Fmexican_food_image.png?alt=media&token=8616b505-c770-4aa0-9fed-de8b70239364"))
            categoryList.add(CategoryThumbModel("Australian","https://firebasestorage.googleapis.com/v0/b/food-clone-c7e62.appspot.com/o/categories%2Faustralian_food_image.jpg?alt=media&token=4e6b4d9b-13d7-4cbc-a2c2-e29d228a9897"))
            categoryList.add(CategoryThumbModel("Canadian","https://firebasestorage.googleapis.com/v0/b/food-clone-c7e62.appspot.com/o/categories%2Fcanadian_food_image.jpg?alt=media&token=be80122c-aca9-4c98-b19a-ffb87bdf8019"))
            categoryList.add(CategoryThumbModel("Chinese","https://firebasestorage.googleapis.com/v0/b/food-clone-c7e62.appspot.com/o/categories%2Fchinese_food_image.png?alt=media&token=f9c5c02a-f606-442d-a5e2-aead36c708ae"))
            categoryList.add(CategoryThumbModel("French","https://firebasestorage.googleapis.com/v0/b/food-clone-c7e62.appspot.com/o/categories%2Ffrench_food_image.png?alt=media&token=d921ad45-6726-4b14-94e5-02d65c01f20d"))
            categoryList.add(CategoryThumbModel("Hungarian","https://firebasestorage.googleapis.com/v0/b/food-clone-c7e62.appspot.com/o/categories%2Fhungarian_food_image.png?alt=media&token=80667bb7-15a7-40fc-8e99-ccb3a63cb6ae"))
            return categoryList
        }
    }
}