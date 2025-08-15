import { Link, useNavigate } from 'react-router-dom';
import { useDeleteProperty, useProperties } from '../../api/property';
export default function PropertyList() {
  const { data, isLoading } = useProperties();
  const del = useDeleteProperty();
  const nav = useNavigate();
  if (isLoading) return <div>Loading...</div>;
  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h4>Properties</h4>
        <Link to="/properties/new" className="btn btn-primary">
          Add Property
        </Link>
      </div>
      <div className="table-responsive">
        <table className="table table-striped table-bordered table-hover table-sm">
          <thead>
            <tr>
              <th>PropertyName</th>
              <th>PropType</th>
              <th>Address</th>
              <th className="text-end">Actions</th>
            </tr>
          </thead>
          <tbody>
            {(data || []).map((p) => (
              <tr key={p.id}>
                <td>{p.name}</td>
                <td>{p.propType}</td>
                <td>{p.address}</td>
                <td className="text-end">
                  <button
                    className="btn btn-outline-primary btn-sm me-2"
                    onClick={() => nav(`/properties/${p.id}`)}
                  >
                    Edit
                  </button>
                  <button
                    className="btn btn-outline-danger btn-sm"
                    onClick={() => p.id && del.mutate(p.id)}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
