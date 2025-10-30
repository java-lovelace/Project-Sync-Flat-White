const apiUrl = "http://localhost:8080/api/projects";

document.addEventListener("DOMContentLoaded", () => {
    getProjects();

    document.getElementById("projectForm").addEventListener("submit", (e) => {
        e.preventDefault();
        const id = document.getElementById("projectId").value;
        if (id) {
            updateProject(id);
        } else {
            createProject();
        }
    });
});

function getProjects() {
    fetch(apiUrl)
        .then(res => res.json())
        .then(data => renderTable(data))
        .catch(err => console.error("Error fetching projects:", err));
}

function renderTable(projects) {
    const tbody = document.querySelector("#projectsTable tbody");
    tbody.innerHTML = "";
    projects.forEach(project => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td>${project.id}</td>
            <td>${project.name}</td>
            <td>${project.description || ''}</td>
            <td>${project.status}</td>
            <td>${project.responsible}</td>
            <td>
            <button class="btn btn-sm btn-warning me-1" onclick="openEditModal(${project.id})">Editar</button>
            <button class="btn btn-sm btn-danger" onclick="deleteProject(${project.id})">Eliminar</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

function openCreateModal() {
    document.getElementById("projectForm").reset();
    document.getElementById("projectId").value = "";
    document.getElementById("projectModalLabel").innerText = "Crear Proyecto";
    document.getElementById("errorMsg").innerText = "";
}

function openEditModal(id) {
    fetch(`${apiUrl}/${id}`)
        .then(res => res.json())
        .then(project => {
            document.getElementById("projectId").value = project.id;
            document.getElementById("name").value = project.name;
            document.getElementById("description").value = project.description;
            document.getElementById("status").value = project.status;
            document.getElementById("responsible").value = project.responsible;
            document.getElementById("projectModalLabel").innerText = "Editar Proyecto";
            new bootstrap.Modal(document.getElementById('projectModal')).show();
        })
        .catch(err => console.error("Error fetching project:", err));
}

function createProject() {
    const project = getFormData();
    fetch(apiUrl, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(project)
    })
        .then(res => {
            if (!res.ok) throw res;
            return res.json();
        })
        .then(() => {
            bootstrap.Modal.getInstance(document.getElementById('projectModal')).hide();
            getProjects();
        })
        .catch(err => showError(err));
}

function updateProject(id) {
    const project = getFormData();
    fetch(`${apiUrl}/${id}`, {
        method: "PUT",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(project)
    })
        .then(res => {
            if (!res.ok) throw res;
            return res.json();
        })
        .then(() => {
            bootstrap.Modal.getInstance(document.getElementById('projectModal')).hide();
            getProjects();
        })
        .catch(err => showError(err));
}

function deleteProject(id) {
    if (!confirm("¿Estás seguro de eliminar este proyecto?")) return;
    fetch(`${apiUrl}/${id}`, {method: "DELETE"})
        .then(res => {
            if (!res.ok) throw res;
            getProjects();
        })
        .catch(err => console.error("Error deleting project:", err));
}

function getFormData() {
    return {
        name: document.getElementById("name").value.trim(),
        description: document.getElementById("description").value.trim(),
        status: document.getElementById("status").value.trim(),
        responsible: document.getElementById("responsible").value.trim()
    };
}

function showError(err) {
    err.json().then(e => {
        document.getElementById("errorMsg").innerText = e.message || "Ocurrió un error";
    }).catch(() => {
        document.getElementById("errorMsg").innerText = "Ocurrió un error";
    });
}
