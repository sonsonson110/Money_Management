package com.example.moneymanagement.ui.navigation

/**
    * Giao diện mô tả hướng đi trong ứng dụng
*/
interface NavigationDestination {
    /**
     * Đường đi là riêng biệt cho mỗi hướng đi, mỗi hướng đi nhận 1 giá trị riêng
     */
    val route: String

    /**
     * Tiêu đề để hiện thị trên App bar tạo điểm đích
     */
    val title: String
}