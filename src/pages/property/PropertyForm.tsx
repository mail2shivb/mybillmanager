import { useParams, useNavigate } from "react-router-dom";
import {
  useCreateProperty,
  useProperty,
  useUpdateProperty,
} from "../../api/property";
import { useEffect, useState } from "react";
import type { Property } from "../../types";

export default function PropertyForm() {
  const { id } = useParams();
  const editing = !!id;
  const { data } = useProperty(id);
  const createMut = useCreateProperty();
  const updateMut = useUpdateProperty(Number(id));
  const nav = useNavigate();
  const [form, setForm] = useState<Property>({
    name: "",
    propType: "Home",
    address: "",
  });
  useEffect(() => {
    if (data) setForm(data);
  }, [data]);
  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (editing) await updateMut.mutateAsync(form);
    else await createMut.mutateAsync(form);
    nav("/");
  };
  return (
    <form onSubmit={onSubmit} className="card p-3">
      <h4>{editing ? "Edit Property" : "New Property"}</h4>
      <div className="mb-3">
        <label className="form-label">PropertyName</label>
        <input
          className="form-control"
          value={form.name}
          onChange={(e) => setForm({ ...form, name: e.target.value })}
          required
        />
      </div>
      <div className="mb-3">
        <label className="form-label">PropType</label>
        <select
          className="form-select"
          value={form.propType}
          onChange={(e) =>
            setForm({ ...form, propType: e.target.value as any })
          }
        >
          <option>Home</option>
          <option>Land</option>
        </select>
      </div>
      <div className="mb-3">
        <label className="form-label">Address</label>
        <input
          className="form-control"
          value={form.address}
          onChange={(e) => setForm({ ...form, address: e.target.value })}
          required
        />
      </div>
      <div className="d-flex gap-2">
        <button className="btn btn-primary" type="submit">
          Save
        </button>
        <button
          className="btn btn-secondary"
          type="button"
          onClick={() => nav(-1)}
        >
          Cancel
        </button>
      </div>
    </form>
  );
}
