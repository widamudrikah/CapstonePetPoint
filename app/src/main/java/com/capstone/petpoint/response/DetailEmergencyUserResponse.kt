package com.capstone.petpoint.response

import com.google.gson.annotations.SerializedName

data class DetailEmergencyUserResponse(

	@field:SerializedName("data")
	val data: List<DataItemDetail>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataItemDetail(

	@field:SerializedName("date_status")
	val dateStatus: String? = null,

	@field:SerializedName("date_accept")
	val dateAccept: Any? = null,

	@field:SerializedName("name_community")
	val nameCommunity: Any? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("date_created")
	val dateCreated: String? = null,

	@field:SerializedName("time_accept")
	val timeAccept: String? = null,

	@field:SerializedName("date_end")
	val dateEnd: String? = null,

	@field:SerializedName("em_id")
	val emId: String? = null,

	@field:SerializedName("name_user")
	val nameUser: String? = null,

	@field:SerializedName("pet_status")
	val petStatus: String? = null,

	@field:SerializedName("time_created")
	val timeCreated: String? = null,

	@field:SerializedName("pet_location")
	val petLocation: String? = null,

	@field:SerializedName("time_end")
	val timeEnd: String? = null,

	@field:SerializedName("time_status")
	val timeStatus: String? = null,

	@field:SerializedName("pet_category")
	val petCategory: String? = null,

	@field:SerializedName("id_community")
	val idCommunity: String? = null,

	@field:SerializedName("evidence_saved")
	val evidenceSaved: String? = null,

	@field:SerializedName("pic_pet")
	val picPet: String? = null,
)
