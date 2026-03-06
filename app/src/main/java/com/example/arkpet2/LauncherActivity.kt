package com.example.arkpet2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.math.max

data class WeightRecord(
    val id: String = UUID.randomUUID().toString(),
    val value: Double,
    val unit: String,
    val timeMillis: Long = System.currentTimeMillis()
)

data class FeedRecord(
    val id: String = UUID.randomUUID().toString(),
    val food: String,
    val amount: String,
    val note: String,
    val timeMillis: Long = System.currentTimeMillis()
)

data class MedicalRecord(
    val id: String = UUID.randomUUID().toString(),
    val hospital: String,
    val diagnosis: String,
    val treatment: String,
    val note: String,
    val timeMillis: Long = System.currentTimeMillis()
)

data class MedicationRecord(
    val id: String = UUID.randomUUID().toString(),
    val medicineName: String,
    val dosage: String,
    val frequency: String,
    val days: String,
    val note: String,
    val timeMillis: Long = System.currentTimeMillis()
)

data class Pet(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val species: String,
    val breed: String,
    val sex: String,
    val birthday: String,
    val note: String,
    val avatarUri: String = "",
    val records: List<WeightRecord> = emptyList(),
    val feeds: List<FeedRecord> = emptyList(),
    val medicals: List<MedicalRecord> = emptyList(),
    val medications: List<MedicationRecord> = emptyList()
)

class LauncherActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ArkPetApp()
                }
            }
        }
    }
}

@Composable
fun ArkPetApp() {
    var pets by remember {
        mutableStateOf(
            listOf(
                Pet(
                    name = "乖乖",
                    species = "猫",
                    breed = "橘白中华宫廷猫",
                    sex = "男",
                    birthday = "2009年1月",
                    note = "已于2024年5月回喵星",
                    records = listOf(
                        WeightRecord(value = 3.0, unit = "kg", timeMillis = System.currentTimeMillis() - 300000),
                        WeightRecord(value = 5.0, unit = "kg", timeMillis = System.currentTimeMillis() - 240000),
                        WeightRecord(value = 8.0, unit = "kg", timeMillis = System.currentTimeMillis() - 180000),
                        WeightRecord(value = 35.0, unit = "g", timeMillis = System.currentTimeMillis() - 120000),
                        WeightRecord(value = 38.0, unit = "g", timeMillis = System.currentTimeMillis() - 60000),
                        WeightRecord(value = 35.0, unit = "g", timeMillis = System.currentTimeMillis())
                    )
                ),
                Pet(
                    name = "点宝",
                    species = "鹦鹉",
                    breed = "浅紫色虎皮",
                    sex = "未知",
                    birthday = "",
                    note = ""
                )
            )
        )
    }

    var selectedPetId by remember { mutableStateOf<String?>(null) }
    var showAddForm by remember { mutableStateOf(false) }
    var showEditForm by remember { mutableStateOf(false) }

    val selectedPet = pets.find { it.id == selectedPetId }

    if (selectedPet == null) {
        PetListScreen(
            pets = pets,
            showAddForm = showAddForm,
            onToggleAddForm = { showAddForm = !showAddForm },
            onAddPet = { newPet ->
                pets = pets + newPet
                showAddForm = false
            },
            onPetClick = { pet -> selectedPetId = pet.id },
            onDeletePet = { petId ->
                pets = pets.filterNot { it.id == petId }
            }
        )
    } else {
        PetDetailScreen(
            pet = selectedPet,
            showEditForm = showEditForm,
            onToggleEditForm = { showEditForm = !showEditForm },
            onBack = {
                selectedPetId = null
                showEditForm = false
            },
            onSaveWeight = { value, unit ->
                pets = pets.map { pet ->
                    if (pet.id == selectedPet.id) pet.copy(
                        records = pet.records + WeightRecord(value = value, unit = unit)
                    ) else pet
                }
            },
            onSaveFeed = { food, amount, note ->
                pets = pets.map { pet ->
                    if (pet.id == selectedPet.id) pet.copy(
                        feeds = pet.feeds + FeedRecord(food = food, amount = amount, note = note)
                    ) else pet
                }
            },
            onDeleteFeed = { feedId ->
                pets = pets.map { pet ->
                    if (pet.id == selectedPet.id) pet.copy(
                        feeds = pet.feeds.filterNot { it.id == feedId }
                    ) else pet
                }
            },
            onSaveMedical = { hospital, diagnosis, treatment, note ->
                pets = pets.map { pet ->
                    if (pet.id == selectedPet.id) pet.copy(
                        medicals = pet.medicals + MedicalRecord(
                            hospital = hospital,
                            diagnosis = diagnosis,
                            treatment = treatment,
                            note = note
                        )
                    ) else pet
                }
            },
            onDeleteMedical = { medicalId ->
                pets = pets.map { pet ->
                    if (pet.id == selectedPet.id) pet.copy(
                        medicals = pet.medicals.filterNot { it.id == medicalId }
                    ) else pet
                }
            },
            onSaveMedication = { medicineName, dosage, frequency, days, note ->
                pets = pets.map { pet ->
                    if (pet.id == selectedPet.id) pet.copy(
                        medications = pet.medications + MedicationRecord(
                            medicineName = medicineName,
                            dosage = dosage,
                            frequency = frequency,
                            days = days,
                            note = note
                        )
                    ) else pet
                }
            },
            onDeleteMedication = { medicationId ->
                pets = pets.map { pet ->
                    if (pet.id == selectedPet.id) pet.copy(
                        medications = pet.medications.filterNot { it.id == medicationId }
                    ) else pet
                }
            },
            onSaveEdit = { updatedPet ->
                pets = pets.map { pet -> if (pet.id == updatedPet.id) updatedPet else pet }
                showEditForm = false
            }
        )
    }
}

@Composable
fun PetListScreen(
    pets: List<Pet>,
    showAddForm: Boolean,
    onToggleAddForm: () -> Unit,
    onAddPet: (Pet) -> Unit,
    onPetClick: (Pet) -> Unit,
    onDeletePet: (String) -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var species by remember { mutableStateOf("") }
    var breed by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var avatarUri by remember { mutableStateOf("") }

    val avatarPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) {}
            avatarUri = uri.toString()
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text("ArkPet 宠物列表", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("共 ${pets.size} 只宠物", fontWeight = FontWeight.Bold)
            Button(onClick = onToggleAddForm) {
                Text(if (showAddForm) "收起" else "添加宠物")
            }
        }

        if (showAddForm) {
            Spacer(modifier = Modifier.height(12.dp))
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("新增宠物资料", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    PetAvatar(avatarUri = avatarUri, sizeDp = 96)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { avatarPicker.launch(arrayOf("image/*")) },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("选择头像") }

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            if (name.isNotBlank() && species.isNotBlank()) {
                                onAddPet(
                                    Pet(
                                        name = name,
                                        species = species,
                                        breed = breed,
                                        sex = sex,
                                        birthday = birthday,
                                        note = note,
                                        avatarUri = avatarUri
                                    )
                                )
                                name = ""
                                species = ""
                                breed = ""
                                sex = ""
                                birthday = ""
                                note = ""
                                avatarUri = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("保存宠物资料") }

                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(name, { name = it }, label = { Text("名字") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(species, { species = it }, label = { Text("物种") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(breed, { breed = it }, label = { Text("品种") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(sex, { sex = it }, label = { Text("性别") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(birthday, { birthday = it }, label = { Text("生日/到家日") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(note, { note = it }, label = { Text("备注") }, modifier = Modifier.fillMaxWidth())
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        pets.forEach { pet ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .clickable { onPetClick(pet) },
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                        PetAvatar(avatarUri = pet.avatarUri, sizeDp = 64)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("名字：${pet.name}", fontWeight = FontWeight.Bold)
                            Text("物种：${pet.species}")
                            Text("品种：${pet.breed}")
                            val latest = pet.records.lastOrNull()
                            if (latest != null) {
                                Text("体重记录：${formatWeight(latest)}（最新）")
                            } else {
                                Text("体重记录：暂无")
                            }
                        }
                    }
                    TextButton(onClick = { onDeletePet(pet.id) }) {
                        Text("删除")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun PetDetailScreen(
    pet: Pet,
    showEditForm: Boolean,
    onToggleEditForm: () -> Unit,
    onBack: () -> Unit,
    onSaveWeight: (Double, String) -> Unit,
    onSaveFeed: (String, String, String) -> Unit,
    onDeleteFeed: (String) -> Unit,
    onSaveMedical: (String, String, String, String) -> Unit,
    onDeleteMedical: (String) -> Unit,
    onSaveMedication: (String, String, String, String, String) -> Unit,
    onDeleteMedication: (String) -> Unit,
    onSaveEdit: (Pet) -> Unit
) {
    val context = LocalContext.current

    var weightInput by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("kg") }

    var foodInput by remember { mutableStateOf("") }
    var amountInput by remember { mutableStateOf("") }
    var feedNoteInput by remember { mutableStateOf("") }

    var hospitalInput by remember { mutableStateOf("") }
    var diagnosisInput by remember { mutableStateOf("") }
    var treatmentInput by remember { mutableStateOf("") }
    var medicalNoteInput by remember { mutableStateOf("") }

    var medicineNameInput by remember { mutableStateOf("") }
    var dosageInput by remember { mutableStateOf("") }
    var frequencyInput by remember { mutableStateOf("") }
    var daysInput by remember { mutableStateOf("") }
    var medicationNoteInput by remember { mutableStateOf("") }

    var editName by remember(pet.id, showEditForm) { mutableStateOf(pet.name) }
    var editSpecies by remember(pet.id, showEditForm) { mutableStateOf(pet.species) }
    var editBreed by remember(pet.id, showEditForm) { mutableStateOf(pet.breed) }
    var editSex by remember(pet.id, showEditForm) { mutableStateOf(pet.sex) }
    var editBirthday by remember(pet.id, showEditForm) { mutableStateOf(pet.birthday) }
    var editNote by remember(pet.id, showEditForm) { mutableStateOf(pet.note) }
    var editAvatarUri by remember(pet.id, showEditForm) { mutableStateOf(pet.avatarUri) }

    val editAvatarPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) {}
            editAvatarUri = uri.toString()
        }
    }

    val sortedRecords = pet.records.sortedByDescending { it.timeMillis }
    val chartRecords = pet.records.sortedBy { it.timeMillis }
    val sortedFeeds = pet.feeds.sortedByDescending { it.timeMillis }
    val sortedMedicals = pet.medicals.sortedByDescending { it.timeMillis }
    val sortedMedications = pet.medications.sortedByDescending { it.timeMillis }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("返回列表")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onToggleEditForm, modifier = Modifier.fillMaxWidth()) {
            Text(if (showEditForm) "收起编辑资料" else "编辑资料")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "宠物详情：${pet.name}",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (showEditForm) {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("编辑宠物资料", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    PetAvatar(avatarUri = editAvatarUri, sizeDp = 96)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { editAvatarPicker.launch(arrayOf("image/*")) },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("更换头像") }

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            if (editName.isNotBlank() && editSpecies.isNotBlank()) {
                                onSaveEdit(
                                    pet.copy(
                                        name = editName,
                                        species = editSpecies,
                                        breed = editBreed,
                                        sex = editSex,
                                        birthday = editBirthday,
                                        note = editNote,
                                        avatarUri = editAvatarUri
                                    )
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("保存修改") }

                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(editName, { editName = it }, label = { Text("名字") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(editSpecies, { editSpecies = it }, label = { Text("物种") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(editBreed, { editBreed = it }, label = { Text("品种") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(editSex, { editSex = it }, label = { Text("性别") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(editBirthday, { editBirthday = it }, label = { Text("生日/到家日") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(editNote, { editNote = it }, label = { Text("备注") }, modifier = Modifier.fillMaxWidth())
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    PetAvatar(avatarUri = pet.avatarUri, sizeDp = 120)
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text("名字：${pet.name}", fontWeight = FontWeight.Bold)
                Text("物种：${pet.species}")
                Text("品种：${pet.breed}")
                Text("性别：${pet.sex.ifBlank { "未填" }}")
                Text("生日/到家日：${pet.birthday.ifBlank { "未填" }}")
                Text("备注：${pet.note.ifBlank { "无" }}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        SectionTitle("记录体重")
        RecordWeightCard(
            weightInput = weightInput,
            onWeightChange = { weightInput = it },
            unit = unit,
            onUnitToggle = { unit = if (unit == "kg") "g" else "kg" },
            onSave = {
                val value = weightInput.toDoubleOrNull()
                if (value != null) {
                    onSaveWeight(value, unit)
                    weightInput = ""
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        SectionTitle("记录喂食")
        FeedCard(
            foodInput = foodInput,
            onFoodChange = { foodInput = it },
            amountInput = amountInput,
            onAmountChange = { amountInput = it },
            noteInput = feedNoteInput,
            onNoteChange = { feedNoteInput = it },
            onSave = {
                if (foodInput.isNotBlank()) {
                    onSaveFeed(foodInput, amountInput, feedNoteInput)
                    foodInput = ""
                    amountInput = ""
                    feedNoteInput = ""
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        SectionTitle("记录医疗")
        MedicalCard(
            hospitalInput = hospitalInput,
            onHospitalChange = { hospitalInput = it },
            diagnosisInput = diagnosisInput,
            onDiagnosisChange = { diagnosisInput = it },
            treatmentInput = treatmentInput,
            onTreatmentChange = { treatmentInput = it },
            noteInput = medicalNoteInput,
            onNoteChange = { medicalNoteInput = it },
            onSave = {
                if (hospitalInput.isNotBlank() || diagnosisInput.isNotBlank()) {
                    onSaveMedical(hospitalInput, diagnosisInput, treatmentInput, medicalNoteInput)
                    hospitalInput = ""
                    diagnosisInput = ""
                    treatmentInput = ""
                    medicalNoteInput = ""
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        SectionTitle("记录用药")
        MedicationCard(
            medicineNameInput = medicineNameInput,
            onMedicineNameChange = { medicineNameInput = it },
            dosageInput = dosageInput,
            onDosageChange = { dosageInput = it },
            frequencyInput = frequencyInput,
            onFrequencyChange = { frequencyInput = it },
            daysInput = daysInput,
            onDaysChange = { daysInput = it },
            noteInput = medicationNoteInput,
            onNoteChange = { medicationNoteInput = it },
            onSave = {
                if (medicineNameInput.isNotBlank()) {
                    onSaveMedication(
                        medicineNameInput,
                        dosageInput,
                        frequencyInput,
                        daysInput,
                        medicationNoteInput
                    )
                    medicineNameInput = ""
                    dosageInput = ""
                    frequencyInput = ""
                    daysInput = ""
                    medicationNoteInput = ""
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("体重曲线", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth().height(220.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (chartRecords.size < 2) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("至少 2 条记录后显示曲线")
                }
            } else {
                WeightChart(records = chartRecords)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HistoryWeightCard(sortedRecords)
        Spacer(modifier = Modifier.height(16.dp))
        HistoryFeedCard(sortedFeeds, onDeleteFeed)
        Spacer(modifier = Modifier.height(16.dp))
        HistoryMedicalCard(sortedMedicals, onDeleteMedical)
        Spacer(modifier = Modifier.height(16.dp))
        HistoryMedicationCard(sortedMedications, onDeleteMedication)

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(title, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun RecordWeightCard(
    weightInput: String,
    onWeightChange: (String) -> Unit,
    unit: String,
    onUnitToggle: () -> Unit,
    onSave: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Button(onClick = onSave, modifier = Modifier.fillMaxWidth()) {
                Text("保存体重")
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = weightInput,
                    onValueChange = onWeightChange,
                    label = { Text("输入体重") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onUnitToggle) { Text(unit) }
            }
        }
    }
}

@Composable
fun FeedCard(
    foodInput: String,
    onFoodChange: (String) -> Unit,
    amountInput: String,
    onAmountChange: (String) -> Unit,
    noteInput: String,
    onNoteChange: (String) -> Unit,
    onSave: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Button(onClick = onSave, modifier = Modifier.fillMaxWidth()) {
                Text("保存喂食记录")
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(foodInput, onFoodChange, label = { Text("食物") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(amountInput, onAmountChange, label = { Text("数量") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(noteInput, onNoteChange, label = { Text("备注") }, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun MedicalCard(
    hospitalInput: String,
    onHospitalChange: (String) -> Unit,
    diagnosisInput: String,
    onDiagnosisChange: (String) -> Unit,
    treatmentInput: String,
    onTreatmentChange: (String) -> Unit,
    noteInput: String,
    onNoteChange: (String) -> Unit,
    onSave: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Button(onClick = onSave, modifier = Modifier.fillMaxWidth()) {
                Text("保存医疗记录")
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(hospitalInput, onHospitalChange, label = { Text("医院/医生") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(diagnosisInput, onDiagnosisChange, label = { Text("诊断") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(treatmentInput, onTreatmentChange, label = { Text("处理") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(noteInput, onNoteChange, label = { Text("备注") }, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun MedicationCard(
    medicineNameInput: String,
    onMedicineNameChange: (String) -> Unit,
    dosageInput: String,
    onDosageChange: (String) -> Unit,
    frequencyInput: String,
    onFrequencyChange: (String) -> Unit,
    daysInput: String,
    onDaysChange: (String) -> Unit,
    noteInput: String,
    onNoteChange: (String) -> Unit,
    onSave: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Button(onClick = onSave, modifier = Modifier.fillMaxWidth()) {
                Text("保存用药记录")
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(medicineNameInput, onMedicineNameChange, label = { Text("药名") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(dosageInput, onDosageChange, label = { Text("剂量") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(frequencyInput, onFrequencyChange, label = { Text("频率") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(daysInput, onDaysChange, label = { Text("天数") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(noteInput, onNoteChange, label = { Text("备注") }, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun HistoryWeightCard(sortedRecords: List<WeightRecord>) {
    Text("体重记录（最近）", fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(8.dp))
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (sortedRecords.isEmpty()) {
                Text("暂无记录")
            } else {
                sortedRecords.take(10).forEach { record ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(formatWeight(record))
                        Text(formatTime(record.timeMillis))
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}

@Composable
fun HistoryFeedCard(sortedFeeds: List<FeedRecord>, onDeleteFeed: (String) -> Unit) {
    Text("喂食记录（最近）", fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(8.dp))
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (sortedFeeds.isEmpty()) {
                Text("暂无喂食记录")
            } else {
                sortedFeeds.take(10).forEach { feed ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("食物：${feed.food}", fontWeight = FontWeight.Bold)
                            Text("数量：${feed.amount.ifBlank { "未填" }}")
                            Text("备注：${feed.note.ifBlank { "无" }}")
                            Text("时间：${formatTime(feed.timeMillis)}")
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(onClick = { onDeleteFeed(feed.id) }) {
                                Text("删除这条喂食记录")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryMedicalCard(sortedMedicals: List<MedicalRecord>, onDeleteMedical: (String) -> Unit) {
    Text("医疗记录（最近）", fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(8.dp))
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (sortedMedicals.isEmpty()) {
                Text("暂无医疗记录")
            } else {
                sortedMedicals.take(10).forEach { medical ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("医院/医生：${medical.hospital.ifBlank { "未填" }}", fontWeight = FontWeight.Bold)
                            Text("诊断：${medical.diagnosis.ifBlank { "未填" }}")
                            Text("处理：${medical.treatment.ifBlank { "未填" }}")
                            Text("备注：${medical.note.ifBlank { "无" }}")
                            Text("时间：${formatTime(medical.timeMillis)}")
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(onClick = { onDeleteMedical(medical.id) }) {
                                Text("删除这条医疗记录")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryMedicationCard(sortedMedications: List<MedicationRecord>, onDeleteMedication: (String) -> Unit) {
    Text("用药记录（最近）", fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(8.dp))
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (sortedMedications.isEmpty()) {
                Text("暂无用药记录")
            } else {
                sortedMedications.take(10).forEach { medication ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("药名：${medication.medicineName}", fontWeight = FontWeight.Bold)
                            Text("剂量：${medication.dosage.ifBlank { "未填" }}")
                            Text("频率：${medication.frequency.ifBlank { "未填" }}")
                            Text("天数：${medication.days.ifBlank { "未填" }}")
                            Text("备注：${medication.note.ifBlank { "无" }}")
                            Text("时间：${formatTime(medication.timeMillis)}")
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(onClick = { onDeleteMedication(medication.id) }) {
                                Text("删除这条用药记录")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PetAvatar(avatarUri: String, sizeDp: Int) {
    if (avatarUri.isBlank()) {
        Box(
            modifier = Modifier
                .size(sizeDp.dp)
                .background(Color(0xFFE0E0E0), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("无头像")
        }
    } else {
        AndroidView(
            factory = { context ->
                ImageView(context).apply {
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }
            },
            update = { imageView ->
                imageView.setImageURI(Uri.parse(avatarUri))
            },
            modifier = Modifier
                .size(sizeDp.dp)
                .background(Color(0xFFE0E0E0), CircleShape)
        )
    }
}

@Composable
fun WeightChart(records: List<WeightRecord>) {
    val normalized = records.map { if (it.unit == "kg") it.value * 1000.0 else it.value }
    val maxValue = normalized.maxOrNull() ?: 1.0
    val minValue = normalized.minOrNull() ?: 0.0
    val range = max(1.0, maxValue - minValue)

    Canvas(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        val w = size.width
        val h = size.height

        val points = normalized.mapIndexed { index, value ->
            val x = if (normalized.size == 1) 0f else index * (w / (normalized.size - 1))
            val y = h - (((value - minValue) / range) * h).toFloat()
            Offset(x, y)
        }

        val path = Path()
        points.forEachIndexed { index, point ->
            if (index == 0) path.moveTo(point.x, point.y) else path.lineTo(point.x, point.y)
        }

        drawPath(path = path, color = Color.Red)

        points.forEach { point ->
            drawCircle(color = Color.Red, radius = 6f, center = point)
        }
    }
}

fun formatTime(timeMillis: Long): String {
    return SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()).format(Date(timeMillis))
}

fun formatWeight(record: WeightRecord): String {
    val valueText = if (record.value % 1.0 == 0.0) record.value.toInt().toString() else record.value.toString()
    return "$valueText ${record.unit}"
}