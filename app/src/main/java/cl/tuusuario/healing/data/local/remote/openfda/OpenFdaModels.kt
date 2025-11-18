package cl.tuusuario.healing.data.local.remote.openfda

// Respuesta raíz de openFDA para drug/label
data class DrugLabelResponse(
    val results: List<DrugLabelItem>?
)

data class DrugLabelItem(
    val openfda: OpenFdaInfo?,
    val purpose: List<String>?,
    val indications_and_usage: List<String>?,
    val warnings: List<String>?
)

data class OpenFdaInfo(
    val generic_name: List<String>?,
    val brand_name: List<String>?
)