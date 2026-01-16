const root = document.body;
const BACKEND_URL =
  root.dataset.backendUrl || "http://localhost:8080/api/predict";
const HEALTH_URL = root.dataset.healthUrl || "http://localhost:8080/api/health";

const $ = (id) => document.getElementById(id);

function showAlert(type, msg) {
  const box = $("alertBox");
  box.className = `alert alert-${type}`;
  box.textContent = msg;
  box.classList.remove("d-none");
}

function hideAlert() {
  $("alertBox").classList.add("d-none");
}

function setLoading(isLoading) {
  $("btnPredict").disabled = isLoading;
  $("btnSpinner").classList.toggle("d-none", !isLoading);
}

function setResultEmpty(isEmpty) {
  const empty = document.getElementById("resultEmpty");
  const content = document.getElementById("resultContent");
  if (!empty || !content) return;

  empty.classList.toggle("d-none", !isEmpty);
  content.classList.toggle("d-none", isEmpty);
}

function resetResultUI() {
  const ids = [
    "outCustomer",
    "outPrevision",
    "outProb",
    "outLabel",
    "riskText",
    "riskHint",
    "debugInfo",
  ];
  ids.forEach((id) => {
    const el = document.getElementById(id);
    if (el) el.textContent = "—";
  });

  const bar = document.getElementById("riskBar");
  if (bar) bar.style.width = "0%";
}

function clearAll() {
  // ocultar alert si existe
  const alertBox = document.getElementById("alertBox");
  if (alertBox) alertBox.classList.add("d-none");

  // reset form
  const form = document.getElementById("predictForm");
  if (form) {
    form.reset();
    form.classList.remove("was-validated");
  }

  // reset result
  resetResultUI();
  setResultEmpty(true);

  // foco al primer campo
  document.getElementById("customer_id")?.focus();
}

function formatProb(p) {
  if (p === undefined || p === null || Number.isNaN(Number(p))) return "—";
  const n = Number(p);
  return `${n.toFixed(3)} (${(n * 100).toFixed(1)}%)`;
}

function planFee(plan) {
  const map = { Basic: 8.99, Standard: 13.99, Premium: 17.99 };
  return map[plan];
}

// ✅ Ping PRO: el frontend consulta el health del BACKEND,
// y el backend decide si el ML está UP o DOWN.
async function pingHealth() {
  try {
    const r = await fetch(HEALTH_URL, { method: "GET" });
    if (!r.ok) throw new Error("Health not ok");

    const h = await r.json();
    // esperado: { status, backend, ml, ... }

    if (h.backend === "UP") {
      $(
        "status-backend"
      ).innerHTML = `<i class="bi bi-circle-fill text-success"></i> Backend: OK`;
    } else {
      $(
        "status-backend"
      ).innerHTML = `<i class="bi bi-circle-fill text-danger"></i> Backend: OFF`;
    }

    if (h.ml === "UP") {
      $(
        "status-ml"
      ).innerHTML = `<i class="bi bi-circle-fill text-success"></i> ML: OK`;
    } else {
      // DEGRADED: backend UP pero ML DOWN
      $(
        "status-ml"
      ).innerHTML = `<i class="bi bi-circle-fill text-danger"></i> ML: OFF`;
    }
  } catch {
    $(
      "status-backend"
    ).innerHTML = `<i class="bi bi-circle-fill text-danger"></i> Backend: OFF`;
    $(
      "status-ml"
    ).innerHTML = `<i class="bi bi-circle-fill text-danger"></i> ML: ?`;
  }
}

function fillAuto() {
  $("customer_id").value = "ed230";
  $("subscription_type").value = "Basic";
  $("payment_method").value = "Debit Card";
  $("watch_hours").value = 5;
  $("last_login_days").value = 4;
  $("number_of_profiles").value = 1;
  $("avg_watch_time_per_day").value = 0.1;
  $("monthly_fee").value = planFee("Basic");
  $("predictForm").classList.remove("was-validated");
}

function readPayload() {
  const subscription = $("subscription_type").value;

  return {
    customer_id: $("customer_id").value.trim(),
    features: {
      subscription_type: subscription,
      payment_method: $("payment_method").value,
      watch_hours: Number($("watch_hours").value),
      last_login_days: Number($("last_login_days").value),
      monthly_fee: Number($("monthly_fee").value),
      number_of_profiles: Number($("number_of_profiles").value),
      avg_watch_time_per_day: Number($("avg_watch_time_per_day").value),
    },
  };
}

function labelToPrevision(label) {
  if (label === "will_churn") return "Va a cancelar";
  if (label === "will_continue") return "Va a continuar";
  return "—";
}

function updateRiskMeter(probability) {
  const bar = document.getElementById("riskBar");
  const txt = document.getElementById("riskText");
  const hint = document.getElementById("riskHint");

  // Resetear el estado de la barra
  bar.style.width = "0%";
  bar.className = "progress-bar";
  txt.textContent = "—";
  hint.textContent = "—";

  // Validar la probabilidad
  if (
    probability === undefined ||
    probability === null ||
    Number.isNaN(Number(probability))
  )
    return;

  const churnRisk = Math.max(0, Math.min(1, Number(probability)));
  const churnPct = Math.round(churnRisk * 100);
  bar.style.width = `${churnPct}%`;

  let level = "Bajo";
  let klass = "bg-success";

  // Definir los niveles de riesgo y los colores de la barra
  if (churnRisk >= 0.7) {
    level = "Alto";
    klass = "bg-danger";
  } else if (churnRisk >= 0.35) {
    level = "Medio";
    klass = "bg-warning";
  }

  // Actualizar la clase de la barra
  bar.classList.add(klass);
  txt.textContent = `${level} (${churnPct}%)`;

  // Mensajes adicionales según el nivel de riesgo
  if (level === "Bajo")
    hint.textContent = "Riesgo bajo de abandono según el modelo.";
  if (level === "Medio")
    hint.textContent =
      "Riesgo medio: conviene monitorear y aplicar retención ligera.";
  if (level === "Alto")
    hint.textContent = "Riesgo alto: recomendar acción inmediata de retención.";
}

function renderResult(apiResponse) {
  // ✅ Mostrar contenido y ocultar estado vacío
  setResultEmpty(false);
  const data = apiResponse.data ?? {};
  const label = data.prediction?.label;
  const prob = data.prediction?.probability;

  $("outCustomer").textContent = data.customer_id ?? "—";
  $("outLabel").textContent = label ?? "—";
  $("outProb").textContent = formatProb(prob);
  $("outPrevision").textContent = data.prevision ?? labelToPrevision(label);
  $(
    "debugInfo"
  ).textContent = `Status: ${apiResponse.status} | Path: ${apiResponse.path}`;

  updateRiskMeter(prob);
}

function validateForm() {
  const form = $("predictForm");
  form.classList.add("was-validated");
  return form.checkValidity();
}

async function predict() {
  hideAlert();

  if (!validateForm()) {
    showAlert(
      "danger",
      "Revisa los campos marcados. Hay valores faltantes o inválidos."
    );
    return;
  }

  setLoading(true);

  const payload = readPayload();

  try {
    const r = await fetch(BACKEND_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });

    const json = await r.json();

    if (!r.ok) {
      const details = JSON.stringify(json.details ?? json, null, 2);
      showAlert(
        "danger",
        `Error (${json.status ?? r.status}): ${
          json.error ?? "Bad Request"
        }. Detalles: ${details}`
      );
      return;
    }

    showAlert("success", "OK: predicción recibida correctamente.");
    renderResult(json);

    // ✅ refresca estado real (desde backend)
    await pingHealth();
  } catch (e) {
    showAlert("warning", `No se pudo conectar al backend. (${e.message})`);
  } finally {
    setLoading(false);
  }
}

document.addEventListener("DOMContentLoaded", () => {
  // ✅ Estado inicial del panel de Resultado
  setResultEmpty(true);
  setResultEmpty(true);
  resetResultUI();

  $("badgeEndpoint").textContent = `Endpoint: POST ${BACKEND_URL}`;
  $("endpointLabel").textContent = BACKEND_URL.replace(
    "http://localhost:8080",
    ""
  );

  document.getElementById("btnClear")?.addEventListener("click", clearAll);

  pingHealth();
  setInterval(pingHealth, 5000);

  $("btnAuto").addEventListener("click", fillAuto);

  $("subscription_type").addEventListener("change", (e) => {
    const fee = planFee(e.target.value);
    if (fee !== undefined) $("monthly_fee").value = fee;
  });

  $("predictForm").addEventListener("submit", (e) => {
    e.preventDefault();
    predict();
  });
});
