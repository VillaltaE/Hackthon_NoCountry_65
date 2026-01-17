const root = document.body;
const BACKEND_URL =
  root.dataset.backendUrl || "http://localhost:8080/api/predict";
const HEALTH_URL = root.dataset.healthUrl || "http://localhost:8080/api/health";

const $ = (id) => document.getElementById(id);

let currentPage = 1;
const totalPages = 5; 

function updatePagination() {
  const currentPageText = document.getElementById('currentPage');
  const btnPrev = document.getElementById('btnPrev');
  const btnNext = document.getElementById('btnNext');

  currentPageText.textContent = `Página ${currentPage}`;

  // Deshabilitar/activar botones
  btnPrev.disabled = currentPage === 1;
  btnNext.disabled = currentPage === totalPages;
}



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

let LAST_HISTORY = [];

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


document.getElementById('btnPrev').addEventListener('click', () => {
  if (currentPage > 1) {
    currentPage--;  // Decrementa la página
    updatePagination();  // Actualiza la UI de paginación
    loadHistory();  // Carga los datos de la nueva página
  }
});

document.getElementById('btnNext').addEventListener('click', () => {
  if (currentPage < totalPages) {
    currentPage++;  // Incrementa la página
    updatePagination();  // Actualiza la UI de paginación
    loadHistory();  // Carga los datos de la nueva página
  }
});


// ================= HISTORIAL =================

async function loadHistory() {
  const customerId =
    document.getElementById("historyCustomerId")?.value?.trim() || "";
  const start = document.getElementById("historyStartDate")?.value || ""; // YYYY-MM-DD
  const end = document.getElementById("historyEndDate")?.value || ""; // YYYY-MM-DD

  const page = currentPage - 1; // Usar la página dinámica
  const size = 10; // Número de registros por página

  let url = "";
  let list = [];

  try {
    // Caso A: Fechas (requiere start y end)
    if (start && end) {
      const startDate = `${start} 00:00:00`;
      const endDate = `${end} 23:59:59`;

      const params = new URLSearchParams({
        startDate,
        endDate,
        page,
        size,
      });

      url = `http://localhost:8080/api/history/filter?${params.toString()}`;

      console.log("[HISTORY] usando FILTER (fechas)", url);

      const res = await fetch(url);
      if (!res.ok) throw new Error("Error al consultar historial (filtro por fecha)");

      const data = await res.json();
      list = Array.isArray(data?.content) ? data.content : data || [];

      // Si además hay customerId, filtramos en frontend
      if (customerId) {
        const cid = customerId.toLowerCase();
        list = list.filter((h) =>
          String(h.customerId || "").toLowerCase().includes(cid)
        );
      }
    }

    // Caso B: Solo cliente
    else if (customerId) {
      const params = new URLSearchParams({ page, size });
      url = `http://localhost:8080/api/history/${encodeURIComponent(customerId)}?${params.toString()}`;

      console.log("[HISTORY] usando POR CLIENTE", url);

      const res = await fetch(url);
      if (!res.ok) throw new Error("Error al consultar historial (por cliente)");

      const data = await res.json();
      list = Array.isArray(data?.content) ? data.content : data || [];
    }

    // Caso C: Sin filtros (últimos 20)
    else {
      const params = new URLSearchParams({ page, size });
      url = `http://localhost:8080/api/history?${params.toString()}`;

      console.log("[HISTORY] usando ULTIMOS", url);

      const res = await fetch(url);
      if (!res.ok) throw new Error("Error al consultar historial");

      const data = await res.json();
      list = Array.isArray(data?.content) ? data.content : data || [];
    }

    LAST_HISTORY = list;
    renderHistory(list);

    // Verificar si no hay registros en la página actual
    if (list.length === 0) {
      // Deshabilitar el botón "Siguiente" si no hay datos
      document.getElementById('btnNext').disabled = true;
      showAlert("warning", "No hay registros para mostrar en esta página.");
    } else {
      // Habilitar el botón "Siguiente" si hay datos
      document.getElementById('btnNext').disabled = false;
    }

    // Verificar si hay más registros en la siguiente página antes de habilitar "Siguiente"
    if (list.length < size) {
      // Si el número de registros es menor al tamaño de la página, deshabilitamos el botón "Siguiente"
      document.getElementById('btnNext').disabled = true;
    }

    // Verificar si estamos en la primera página y deshabilitar el botón "Anterior"
    document.getElementById('btnPrev').disabled = currentPage === 1;

  } catch (e) {
    console.error("[HISTORY] ERROR", e);
    showAlert("danger", e.message);
  }
}





function renderHistory(list) {
  const table = document.getElementById("historyTable");
  const body = document.getElementById("historyTbody");
  const empty = document.getElementById("historyEmpty");

  body.innerHTML = "";

  if (!list || list.length === 0) {
    table.classList.add("d-none");
    empty.classList.remove("d-none");
    return;
  }

  function riskBadge(label) {
    if (!label) return "-";
    if (label.toLowerCase().includes("alto"))
      return `<span class="badge bg-danger">Riesgo alto</span>`;
    if (label.toLowerCase().includes("medio"))
      return `<span class="badge bg-warning text-dark">Riesgo medio</span>`;
    return `<span class="badge bg-success">Riesgo bajo</span>`;
  }

  empty.classList.add("d-none");
  table.classList.remove("d-none");

list.forEach((h) => {
  const safe = encodeURIComponent(JSON.stringify(h)); // ✅ NUEVA LÍNEA

  const tr = document.createElement("tr");
  tr.innerHTML = `
    <td>${h.customerId}</td>
    <td>${new Date(h.createdAt).toLocaleString()}</td>
    <td>${riskBadge(h.predictionLabel)}</td>
    <td><code class="text-secondary">${h.label}</code></td>
    <td class="text-end">${(h.probability * 100).toFixed(1)}%</td>
    <td class="text-end">
      <button
        class="btn btn-sm btn-outline-light"
        data-history="${safe}"
        onclick="openHistoryDetail(this)">
        Ver
      </button>
    </td>
  `;
  body.appendChild(tr);
  });
}


function openHistoryDetail(btn) {
  const h = JSON.parse(decodeURIComponent(btn.dataset.history));
  const body = document.getElementById("historyDetailBody");

  body.innerHTML = `
    <div class="row g-3">
      <div class="col-12"><b>Cliente:</b> ${h.customerId}</div>
      <div class="col-12"><b>Fecha:</b> ${new Date(h.createdAt).toLocaleString()}</div>
      <hr class="border-secondary">
      <div class="col-md-4"><b>Plan:</b> ${h.subscriptionType}</div>
      <div class="col-md-4"><b>Pago:</b> ${h.paymentMethod}</div>
      <div class="col-md-4"><b>Tarifa:</b> $${h.monthlyFee}</div>
      <div class="col-md-3"><b>Horas vistas:</b> ${h.watchHours}</div>
      <div class="col-md-3"><b>Días sin acceso:</b> ${h.lastLoginDays}</div>
      <div class="col-md-3"><b>Perfiles:</b> ${h.numberOfProfiles}</div>
      <div class="col-md-3"><b>Prom. diario:</b> ${h.avgWatchTimePerDay}</div>
      <hr class="border-secondary">
      <div class="col-md-4"><b>Etiqueta ML:</b> ${h.label}</div>
      <div class="col-md-4"><b>Resultado:</b> ${h.predictionLabel}</div>
      <div class="col-md-4"><b>Probabilidad:</b> ${(h.probability * 100).toFixed(1)}%</div>
    </div>
  `;

  new bootstrap.Modal(document.getElementById("historyDetailModal")).show();
}


function exportHistoryCSV() {
  if (!LAST_HISTORY || LAST_HISTORY.length === 0) {
    showAlert(
      "warning",
      "No hay datos para exportar. Primero presiona Buscar."
    );
    return;
  }

  const rows = LAST_HISTORY.map((h) => ({
    customerId: h.customerId,
    createdAt: h.createdAt,
    subscriptionType: h.subscriptionType,
    paymentMethod: h.paymentMethod,
    monthlyFee: h.monthlyFee,
    watchHours: h.watchHours,
    lastLoginDays: h.lastLoginDays,
    numberOfProfiles: h.numberOfProfiles,
    avgWatchTimePerDay: h.avgWatchTimePerDay,
    label: h.label,
    predictionLabel: h.predictionLabel,
    probability: h.probability,
  }));

  const headers = Object.keys(rows[0]);
  const csv = [
    headers.join(","),
    ...rows.map((r) =>
      headers.map((k) => `"${String(r[k]).replaceAll('"', '""')}"`).join(",")
    ),
  ].join("\n");

  const blob = new Blob([csv], { type: "text/csv;charset=utf-8;" });
  const url = URL.createObjectURL(blob);

  const a = document.createElement("a");
  a.href = url;
  a.download = `historial_churn_${new Date().toISOString().slice(0, 10)}.csv`;
  document.body.appendChild(a);
  a.click();
  a.remove();
  URL.revokeObjectURL(url);
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
  resetResultUI();

  $("badgeEndpoint").textContent = `Endpoint: POST ${BACKEND_URL}`;
  $("endpointLabel").textContent = BACKEND_URL.replace("http://localhost:8080", "");

  // Llamada inicial a la paginación
  updatePagination(); // Actualizar la paginación al cargar la página
  loadHistory(); // Cargar datos iniciales de la página actual

  // Manejo de eventos
  document
    .getElementById("btnHistorySearch")
    ?.addEventListener("click", loadHistory);

  document
    .getElementById("btnHistoryExport")
    ?.addEventListener("click", exportHistoryCSV);

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

