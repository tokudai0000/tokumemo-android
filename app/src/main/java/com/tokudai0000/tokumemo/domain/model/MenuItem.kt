package com.tokudai0000.tokumemo.domain.model

data class MenuItem(
    val title: String,
    val id: Type,
    val image: Int,
    val url: String?
) {
    enum class Type {
        CourseManagement,
        Manaba,
        Mail,
        AcademicRelated,
        LibraryRelated,
        Etc,
    }
}