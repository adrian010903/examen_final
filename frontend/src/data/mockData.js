export const mockHorses = [
  { id: 1, nombre: 'Relampago', identificador: 'CAB-001', edad: 7, raza: 'Andaluz', sexo: 'Macho', peso: 470, fotoUrl: 'https://images.unsplash.com/photo-1553284965-83fd3e82fa5a?auto=format&fit=crop&w=900&q=80' },
  { id: 2, nombre: 'Aurora', identificador: 'CAB-002', edad: 5, raza: 'Pura sangre', sexo: 'Hembra', peso: 430, fotoUrl: 'https://images.unsplash.com/photo-1527153857715-3908f2bae5e8?auto=format&fit=crop&w=900&q=80' },
  { id: 3, nombre: 'Noble', identificador: 'CAB-003', edad: 10, raza: 'Criollo', sexo: 'Macho', peso: 455, fotoUrl: 'https://images.unsplash.com/photo-1450052590821-8bf91254a353?auto=format&fit=crop&w=900&q=80' }
];

export const mockEmployees = [
  { id: 1, nombre: 'Mariana Solis', rol: 'VETERINARIO', contacto: 'mariana@caballeriza.test' },
  { id: 2, nombre: 'Carlos Vega', rol: 'CUIDADOR', contacto: '8888-1040' },
  { id: 3, nombre: 'Ana Rojas', rol: 'ENTRENADOR', contacto: 'ana@caballeriza.test' }
];

export const mockMedicalRecords = [
  { id: 1, tipo: 'Vacunacion', descripcion: 'Refuerzo anual aplicado sin reaccion adversa.', alergias: 'Ninguna', observaciones: 'Control en 12 meses', fecha: '2026-06-10', fechaProxima: '2026-07-10', responsable: 'Mariana Solis', caballoId: 1, nombreCaballo: 'Relampago' },
  { id: 2, tipo: 'Tratamiento', descripcion: 'Chequeo general y control de peso.', alergias: 'Polvo', observaciones: 'Mejorar ventilacion del establo', fecha: '2026-06-14', fechaProxima: '2026-06-25', responsable: 'Mariana Solis', caballoId: 2, nombreCaballo: 'Aurora' }
];

export const mockTasks = [
  { id: 1, descripcion: 'Limpieza de establo norte', fecha: '2026-06-20', completada: false, empleadoId: 2, nombreEmpleado: 'Carlos Vega', caballoId: 1, nombreCaballo: 'Relampago' },
  { id: 2, descripcion: 'Entrenamiento de pista', fecha: '2026-06-21', completada: true, empleadoId: 3, nombreEmpleado: 'Ana Rojas', caballoId: 2, nombreCaballo: 'Aurora' }
];

export const mockShifts = [
  { id: 1, fecha: '2026-06-20', horaInicio: '08:00', horaFin: '12:00', empleadoId: 2, nombreEmpleado: 'Carlos Vega' },
  { id: 2, fecha: '2026-06-21', horaInicio: '13:00', horaFin: '17:00', empleadoId: 3, nombreEmpleado: 'Ana Rojas' }
];

export const mockInventory = [
  { id: 1, nombre: 'Heno premium', tipo: 'Alimento', cantidad: 18, stockMinimo: 15, unidad: 'fardos', stockBajo: false },
  { id: 2, nombre: 'Desinfectante', tipo: 'Limpieza', cantidad: 4, stockMinimo: 6, unidad: 'litros', stockBajo: true },
  { id: 3, nombre: 'Suplemento mineral', tipo: 'Salud', cantidad: 8, stockMinimo: 8, unidad: 'kg', stockBajo: true }
];

export const mockFeedingPlans = [
  { id: 1, tipoAlimento: 'Heno premium', cantidad: '6 kg', frecuencia: '2 veces al dia', observaciones: 'Despues del ejercicio', caballoId: 1, nombreCaballo: 'Relampago' },
  { id: 2, tipoAlimento: 'Concentrado', cantidad: '3 kg', frecuencia: 'Diario', observaciones: 'Agregar suplemento', caballoId: 2, nombreCaballo: 'Aurora' }
];
