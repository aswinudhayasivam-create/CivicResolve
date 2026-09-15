function Submit({user,go}) {
  const [form,setForm] = useState({
    title:'',
    description:'',
    category:'1',
    priority:'MEDIUM',
    location:''
  });

  const [msg,setMsg] = useState('');
  const [loading,setLoading] = useState(false);

  const categories = [
    { id: 1, name: 'Roads' },
    { id: 2, name: 'Street Lights' },
    { id: 3, name: 'Garbage' },
    { id: 4, name: 'Water Supply' },
    { id: 5, name: 'Electricity' },
    { id: 6, name: 'Drainage' },
    { id: 7, name: 'Public Health' },
    { id: 8, name: 'Public Transport' }
  ];

  const send = async (e) => {
    e.preventDefault();

    if (!user) {
      go('login');
      return;
    }

    setLoading(true);
    setMsg('');

    try {
      const response = await fetch(API + '/complaints', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + localStorage.getItem('token')
        },
        body: JSON.stringify({
          title: form.title,
          description: form.description,
          priority: form.priority,
          location: form.location,
          citizen: {
            id: user.userId
          },
          category: {
            id: Number(form.category)
          }
        })
      });

      const responseText = await response.text();

      let data = null;

      if (responseText.trim()) {
        try {
          data = JSON.parse(responseText);
        } catch {
          data = null;
        }
      }

      if (!response.ok) {
        throw new Error(
          data?.message ||
          data?.error ||
          responseText ||
          `Server error (${response.status})`
        );
      }

      if (!data) {
        throw new Error(
          'The server accepted the request but returned an empty response.'
        );
      }

      setMsg(
        'Complaint submitted successfully! Tracking number: ' +
        data.trackingNumber
      );

      setForm({
        title:'',
        description:'',
        category:'1',
        priority:'MEDIUM',
        location:''
      });

    } catch (error) {
      console.error('Complaint submission error:', error);
      setMsg(error.message || 'Unable to submit complaint.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <section className="max-w-3xl mx-auto px-5 pt-14">
      <h2 className="text-5xl font-black">
        Report a public issue
      </h2>

      <p className="text-slate-400 mt-2">
        Provide enough detail for an authority to act quickly.
      </p>

      <form
        onSubmit={send}
        className="glass rounded-3xl p-7 mt-7 space-y-5"
      >

        <input
          className="input"
          placeholder="Complaint title"
          required
          value={form.title}
          onChange={e =>
            setForm({...form,title:e.target.value})
          }
        />

        <textarea
          className="input min-h-40"
          placeholder="Describe the issue, what happened and where..."
          required
          value={form.description}
          onChange={e =>
            setForm({...form,description:e.target.value})
          }
        />

        <div className="grid md:grid-cols-2 gap-4">

          <select
            className="input"
            value={form.category}
            onChange={e =>
              setForm({...form,category:e.target.value})
            }
          >
            {categories.map(category => (
              <option
                key={category.id}
                value={category.id}
                className="bg-slate-900"
              >
                {category.name}
              </option>
            ))}
          </select>

          <select
            className="input"
            value={form.priority}
            onChange={e =>
              setForm({...form,priority:e.target.value})
            }
          >
            {['LOW','MEDIUM','HIGH','URGENT'].map(priority => (
              <option
                key={priority}
                value={priority}
                className="bg-slate-900"
              >
                {priority}
              </option>
            ))}
          </select>

        </div>

        <input
          className="input"
          placeholder="Location / landmark"
          value={form.location}
          onChange={e =>
            setForm({...form,location:e.target.value})
          }
        />

        <button
          type="submit"
          disabled={loading}
          className="rounded-xl bg-cyan-300 text-slate-950 px-5 py-3 font-bold hover:scale-[1.02] transition disabled:opacity-50 disabled:cursor-not-allowed"
        >
          {loading ? 'Submitting...' : 'Submit grievance'}
        </button>

        {msg && (
          <p className="text-cyan-200">
            {msg}
          </p>
        )}

      </form>
    </section>
  );
}