package ru.studiq.mcashier.model


enum class UserItemType {
    unassigned, admin, cashier, example
}
class UserItem: java.io.Serializable {
    var userType: UserItemType = UserItemType.unassigned
    var ID: Int = 0
    var staffType: Int
    var UID: String
    var userName: String
    var statusName: String
    var departmentName: String
    var profileImageUrl: String


    constructor(usertype: UserItemType, id: Int, stafftype: Int, uid: String, username: String, statusname: String, departmentname: String, profileimageurl: String) {
        this.userType = usertype
        this.ID = id
        this.staffType = stafftype
        this.UID = uid
        this.userName = username
        this.statusName = statusname
        this.departmentName = departmentname
        this.profileImageUrl = profileimageurl
    }
}