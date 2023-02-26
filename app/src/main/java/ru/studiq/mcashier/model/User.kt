package ru.studiq.mcashier.model

import android.os.Parcel
import android.os.Parcelable

enum class UserType {
    unassigned, admin, cashier, example
}
class User : Parcelable {
    var userType: UserType = UserType.unassigned
    var ID: Int = 0
    var staffType: Int
    var UID: String
    var userName: String
    var statusName: String
    var departmentName: String
    var profileImageUrl: String


    constructor(usertype: UserType, id: Int, stafftype: Int, uid: String, username: String, statusname: String, departmentname: String, profileimageurl: String) {
        this.userType = usertype
        this.ID = id
        this.staffType = stafftype
        this.UID = uid
        this.userName = username
        this.statusName = statusname
        this.departmentName = departmentname
        this.profileImageUrl = profileimageurl
    }
    override fun describeContents(): Int {
        return 0
    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(userType.ordinal)
        parcel.writeInt(ID)
        parcel.writeInt(staffType)
        parcel.writeString(UID)
        parcel.writeString(userName)
        parcel.writeString(statusName)
        parcel.writeString(departmentName)
        parcel.writeString(profileImageUrl)
    }
    private constructor(parcel: Parcel) {
        userType = UserType.values()[parcel.readInt()]
        ID = parcel.readInt() ?: -1
        staffType = parcel.readInt() ?: -1
        UID = parcel.readString() ?: ""
        userName = parcel.readString() ?: ""
        statusName = parcel.readString() ?: ""
        departmentName = parcel.readString() ?: ""
        profileImageUrl = parcel.readString() ?: ""
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}