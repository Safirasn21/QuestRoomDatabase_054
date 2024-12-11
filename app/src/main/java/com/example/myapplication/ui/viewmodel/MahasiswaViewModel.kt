package com.example.myapplication.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.entity.Mahasiswa
import com.example.myapplication.repository.LocalRepositoryMhs
import com.example.myapplication.repository.RepositoryMhs
import kotlinx.coroutines.launch

class MahasiswaViewModel(private val repositoryMhs: RepositoryMhs) : ViewModel(){
    var uiState by mutableStateOf(MhsUIState())
    fun updateState(MahasiswaEvent: MahasiswaEvent){
        uiState = uiState.copy(
            mahasiswaEvent = MahasiswaEvent,
        )
    }

    private fun validateFields(): Boolean{
        val event = uiState.mahasiswaEvent
        val errorState = FormErrorState(
            nim = if (event.nim.isNotEmpty()) null else "NIM tidak boleh kosong!",
            nama = if (event.nama.isNotEmpty()) null else "Nama tidak boleh kosong!",
            jenisKelamin = if (event.jenisKelamin.isNotEmpty()) null else "Jenis Kelamin tidak boleh kosong!",
            alamat = if (event.alamat.isNotEmpty()) null else "Alamat tidak boleh kosong!",
            kelas = if (event.kelas.isNotEmpty()) null else "Kelas tidak boleh kosong!",
            angkatan = if (event.angkatan.isNotEmpty()) null else "Angkatan tidak boleh kosong!"
        )
        uiState = uiState.copy(isEntryValid = errorState)
        return errorState.isValid()
    }

    fun saveData(){
        val currentEvent = uiState.mahasiswaEvent

        if (validateFields()){
            viewModelScope.launch {
                try{
                    repositoryMhs.insertMhs(currentEvent.toMahasiswaEntity())
                    uiState = uiState.copy(
                        snackBarMesssage = "Data berhasil disimpan",
                        mahasiswaEvent = MahasiswaEvent(),
                        isEntryValid  = FormErrorState()
                    )
                } catch (e: Exception){
                    uiState = uiState.copy(
                        snackBarMesssage = "Data gagal disimpan"
                    )
                }
            }
        }
        else {
            uiState = uiState.copy(
                snackBarMesssage = "Input tidak valid. periksa kembali data anda"
            )
        }
    }
    fun resetsnackBarMessage(){
        uiState = uiState.copy(snackBarMesssage = null)
    }
}

data class MhsUIState(
    val mahasiswaEvent: MahasiswaEvent = MahasiswaEvent(),
    val isEntryValid: FormErrorState = FormErrorState(),
    val snackBarMesssage: String? = null
)

data class FormErrorState(
    val nim: String? = null,
    val nama: String? = null,
    val jenisKelamin: String? = null,
    val alamat: String? = null,
    val kelas: String? = null,
    val angkatan: String? = null,
){
    fun isValid(): Boolean{
        return nim == null && nama == null && jenisKelamin == null && alamat == null
                && kelas == null && angkatan == null
    }
}

fun MahasiswaEvent.toMahasiswaEntity(): Mahasiswa = Mahasiswa(
    nim = nim,
    nama = nama,
    jenisKelamin = jenisKelamin,
    alamat = alamat,
    kelas = kelas,
    angkatan = angkatan
)

data class MahasiswaEvent(
    val nim: String =" ",
    val nama: String =" ",
    val jenisKelamin: String =" ",
    val alamat: String =" ",
    val kelas: String =" ",
    val angkatan: String =" "
)